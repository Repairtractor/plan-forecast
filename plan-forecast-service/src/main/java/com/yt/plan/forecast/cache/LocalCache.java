package com.yt.plan.forecast.cache;

import cn.hutool.core.collection.CollUtil;

import java.util.*;

public interface LocalCache<K, V, T> extends Cache<K, V, T> {


    @Override
    void put(Collection<T> list);


    @Override
    List<T> sourceAll(Collection<K> keys);

    @Override
    Map<K, V> asMap(Collection<K> keys);


    @Override
    default void put(T t) {
        put(Collections.singletonList(t));
    }


    @Override
    default List<V> get(Collection<K> keys) {
        return new ArrayList<>(asMap(keys).values());
    }


    @Override
    default Optional<V> optionalGet(K k) {
        return Optional.ofNullable(get(k));
    }

    @Override
    default V get(K k) {
        if (CollUtil.isEmpty(get(Collections.singletonList(k)))) {
            return null;
        }
        return get(Collections.singletonList(k)).get(0);
    }
}
