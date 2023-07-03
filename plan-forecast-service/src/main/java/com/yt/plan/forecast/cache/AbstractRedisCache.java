package com.yt.plan.forecast.cache;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.redisson.api.RBuckets;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.yt.plan.forecast.cache.LogEnum.CACHE_INFO;
import static java.util.stream.Collectors.toList;

/**
 * @param <V> 缓存值类型
 * @param <T> 缓存实体
 * @Description: redis缓存抽象类
 */
@EnableConfigurationProperties(RedisCacheConstant.class)
public abstract class AbstractRedisCache<V, T> implements RedisCache<V, T> {

    //缓存key前缀
    protected String keyPath;

    //keyValue获取器
    private final GetSetter<T, String, V> getSetter;

    //是否开启重试同步器
    private final boolean isRetrySynchronizer;

    //重试同步器
    private RedisRetrySynchronizer<V> redisRetrySynchronizer;

    //缓存常量
    private final CacheConstant cacheConstant;


    private final String name;


    @Resource
    private RedisCacheConstant redisCacheConstant;


    @Resource
    private RedissonClient redisson;


    @Resource
    private RedisLockComponent redisLockComponent;


    /**
     * @param name                缓存器名称
     * @param getSetter           keyValue获取器
     * @param isRetrySynchronizer 缓存同步器，默认false，不开启时不会进入同步机制，一致性较差，性能较好，存在缓存穿透问题，建议开启
     */
    protected AbstractRedisCache(String name, GetSetter<T, String, V> getSetter, CacheConstant cacheConstant, boolean isRetrySynchronizer) {
        this.getSetter = getSetter;
        this.isRetrySynchronizer = isRetrySynchronizer;
        this.cacheConstant = cacheConstant;
        this.name = name;
    }

    @PostConstruct
    public void init() {
        this.keyPath = redisCacheConstant.getPATH() + name + ":";
        //开启重试同步器
        if (isRetrySynchronizer) {
            redisRetrySynchronizer = Objects.equals(redisCacheConstant.isDistributed(), true) ?
                    new MultipleRedisRetrySynchronizer<>(keyPath, redisson, cacheConstant, redisLockComponent) :
                    new SimpleRetrySynchronizer<>(keyPath, redisson, cacheConstant, redisLockComponent.redisCacheConstant);
        }
    }


    protected AbstractRedisCache(String name, GetSetter<T, String, V> getSetter, CacheConstant cacheConstant) {
        this(name, getSetter, cacheConstant, false);
    }

    protected AbstractRedisCache(String name, GetSetter<T, String, V> getSetter) {
        this(name, getSetter, new CacheConstant(100, 60, TimeUnit.SECONDS));
    }


    @Override
    public void removeKey(Collection<String> codes) {
        CACHE_INFO.info("开始删除redis缓存,keys：{}", codes);
        List<String> keys = codes.stream().map(it -> keyPath + it).collect(toList());
        try {
            redisson.getKeys().delete(keys.toArray(new String[0]));
        } catch (Exception exception) {
            CACHE_INFO.info("删除redis缓存失败了，移送队列尝试重试，keys:{}", codes);

            codes.forEach(it -> retry(it, null));
        }
    }

    private void retry(String k, V v) {
        if (isRetrySynchronizer)
            redisRetrySynchronizer.retry(k, v);
    }


    @Override
    public void put(Collection<T> list) {
        CACHE_INFO.info("开始添加数据，本次数据量：{}", list.size());
        try {
            Map<String, String> redisKeyValueMap = list.stream().collect(Collectors.toMap(getSetter.keyGetter.andThen(it -> keyPath + it), getSetter.valueGetter.andThen(JSONUtil::toJsonStr)));
            RBuckets buckets = redisson.getBuckets();
            buckets.set(redisKeyValueMap);
            syncExpire(redisKeyValueMap);
        } catch (Exception exception) {
            CACHE_INFO.error("批量添加数据失败，移送队列进行重试");
            list.forEach(it -> retry(getSetter.keyGetter.apply(it), getSetter.valueGetter.apply(it)));
        }
    }

    /**
     * 设置超时时间
     *
     * @param redisKeyValueMap
     */
    private void syncExpire(Map<String, String> redisKeyValueMap) {
        ConcurrencyUtil.execute(() -> {
            redisKeyValueMap.keySet().forEach(it -> redisson.getKeys().expire(it, cacheConstant.expireTime, cacheConstant.timeUnit));
        });
    }


    /**
     * @param keys           redis的key集合
     * @param weatherToReSet 有没有命中的key，是否尝试从原数据添加
     */
    private Map<String, V> get(Collection<String> keys, boolean weatherToReSet) {
        //打印日志，日志截取key前一百
        CACHE_INFO.info("开始从缓存获取数据，keys总大小:{},查询keys:{}", keys.size(),CollUtil.sub(keys, 0, 100));

        //如果开启了重试同步器，且当前正在进行同步，直接走源数据
        if (isRetrySynchronizer && !redisRetrySynchronizer.isReady(keys)) {
            return retrySetAndGetValues(keys);
        }
        List<String> list = keys.stream().map(it -> keyPath + it).collect(toList());

        RBuckets buckets = redisson.getBuckets();
        Map<String, String> map = buckets.get(list.toArray(new String[0]));

        //获取没有命中的key
        List<String> reKeys = keys.stream().filter(it -> !map.containsKey(keyPath + it)).collect(toList());

        //如果没有命中的key为空，直接返回，或者不需要从原数据添加
        if (CollUtil.isEmpty(reKeys) || !weatherToReSet) {
            Map<String, V> result = new HashMap<>();
            map.forEach((k, v) -> {
                result.put(k.replace(keyPath, ""), JSONUtil.isJson(v) ?
                        JSONUtil.toBean(v, new TypeReference<V>() {
                        },false): (V) v);
            });
            CACHE_INFO.info("查询缓存结束。本次缓存查询结果数量:{}", result.size());
            return result;
        }
        CACHE_INFO.info("没有全部命中缓存，本次缓存查询keys数量：{}，未命中缓存keys数量：{}", keys.size(), reKeys.size());
        CACHE_INFO.info("开始从原数据添加缓存，keys:{}", CollUtil.sub(reKeys, 0, 100));
        retrySet(reKeys);
        return get(keys, false);
    }

    /**
     * 重新设置value值
     *
     * @param codes
     * @return 返回value
     */
    private void retrySet(List<String> codes) {
        retrySetAndGetValues(codes);
    }


    //todo 这里是否有多线程问题呢？有，会重复建立缓存，但是问题在于锁什么？怎么判定重复？
    private Map<String, V> retrySetAndGetValues(Collection<String> codes) {

        try {
            //获取锁成功，进行缓存构建,失败挂起当前线程等待锁线程释放锁
            if (!isRetrySynchronizer || redisRetrySynchronizer.lock(codes)) {
                List<T> dates = sourceAll(codes);
                put(dates);
                return dates.stream().collect(Collectors.toMap(getSetter.keyGetter, getSetter.valueGetter));
            }
            return get(codes, false);
        } catch (Exception ex) {
            CACHE_INFO.error("构建缓存失败，本次构建keys：{}", CollUtil.join(codes, StrUtil.COMMA));
            throw ex;
        } finally {
            if (isRetrySynchronizer)
                redisRetrySynchronizer.unLock(codes);
        }
    }

    @Override
    public Map<String, V> asMap(Collection<String> keys) {
        return get(keys, true);
    }

    @Override
    public List<V> get(Collection<String> keys) {
        return RedisCache.super.get(keys);
    }
}
