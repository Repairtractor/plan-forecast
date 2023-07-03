package com.yt.plan.forecast.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 缓存的最高抽象接口，规定了缓存的基本操作
 *
 * @param <K>  缓存的key
 * @param <V> 缓存的value
 * @param <T> 缓存的源数据实体
 */
public interface Cache<K, V, T> {
    /**
     * 删除缓存
     * @param keys
     */
    void removeKey(Collection<K> keys);

    /**
     * 设置缓存
     * @param list
     */
    void put(Collection<T> list);


    /**
     * 设置缓存
     * @param t
     */
    void put(T t);


    /**
     * 获取缓存
     * @param keys
     * @return
     */
    List<V> get(Collection<K> keys);

    /**
     * 获取源数据
     * @param keys
     * @return
     */
    List<T> sourceAll(Collection<K> keys);

    /**
     * 将缓存转换为map
     * @param keys
     * @return
     */
    Map<K,V> asMap(Collection<K> keys);

    /**
     * 获取单个缓存 ,返回Optional
     * @param k
     * @return
     */
    Optional<V> optionalGet(K k);

    /**
     * 获取单个缓存 ,返回Optional
     * @param k
     * @return
     */
    V get(K k);


}
