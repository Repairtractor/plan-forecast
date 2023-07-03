package com.yt.plan.forecast.cache;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.yt.plan.forecast.cache.LogEnum.CACHE_INFO;


public abstract class AbstractLocalCache<K, V, T> implements LocalCache<K, V, T> {

    private final GetSetter<T, K, V> getSetter;

    private LoadingCache<K, V> cache;

    private final CacheConstant cacheConstant;

    private static final CacheConstant CACHE_DEFAULT = new CacheConstant(1000, 1, TimeUnit.DAYS);


    protected CacheLoader<K, V> getCacheCallBack() {
        return new CacheLoader<K, V>() {
            @Override
            public V load(K key) throws Exception {
                Map<K, V> kvMap = getCacheKeyAndValue(Collections.singletonList(key));
                if (MapUtil.isEmpty(kvMap)) {
                    return null;
                }
                return kvMap.get(key);
            }

            @Override
            public @NonNull Map<@NonNull K, @NonNull V> loadAll(@NonNull Iterable<? extends @NonNull K> keys) throws Exception {
                List<K> list = new ArrayList<>();
                keys.forEach(list::add);
                CACHE_INFO.info("本地缓存没有命中，从数据库获取数据，keys：{}", CollUtil.join(keys,","));
                return getCacheKeyAndValue(list);
            }

        };
    }


    protected AbstractLocalCache(GetSetter<T, K, V> getSetter, CacheConstant cacheConstant) {
        this.getSetter = getSetter;
        this.cacheConstant = cacheConstant;
        initCache();
    }

    private void initCache() {
        CACHE_INFO.info("初始化本地缓存，缓存大小：{}，缓存超时时间：{} 单位:{}", getCacheConstant().maxSize, getCacheConstant().expireTime,getCacheConstant().timeUnit.name());
        this.cache = Caffeine.newBuilder()
                .maximumSize(getCacheConstant().maxSize)
                .expireAfterWrite(getCacheConstant().expireTime, getCacheConstant().timeUnit)
                .build(getCacheCallBack());
    }

    @Override
    public void removeKey(Collection<K> keys) {
        cache.invalidateAll(keys);
    }

    @Override
    public void put(Collection<T> list) {
        cache.putAll(list.stream().collect(Collectors.toMap(getSetter.keyGetter, getSetter.valueGetter)));
    }


    @Override
    public Map<K, V> asMap(Collection<K> keys) {
        try {
            return cache.getAll(keys);
        } catch (Exception e) {
            throw new RuntimeException("从缓存获取数据失败了");
        }
    }

    protected Map<K, V> getCacheKeyAndValue(List<K> keys) {
        return sourceAll(keys).stream().collect(Collectors.toMap(getSetter.keyGetter, getSetter.valueGetter));
    }


    protected CacheConstant getCacheConstant() {
        return cacheConstant == null ? CACHE_DEFAULT : cacheConstant;
    }

}
