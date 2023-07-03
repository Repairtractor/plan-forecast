package com.yt.plan.forecast.cache;


import cn.hutool.core.collection.CollUtil;

import java.util.*;

public interface RedisCache<V,T> extends Cache<String,V,T> {
    @Override
    void put(Collection<T> list);


    @Override
    List<T> sourceAll(Collection<String> keys);

    @Override
    Map<String, V> asMap(Collection<String> keys);


    @Override
    default void put(T t) {
        put(Collections.singletonList(t));
    }


    @Override
    default List<V> get(Collection<String> keys) {
        return new ArrayList<>(asMap(keys).values());
    }


    @Override
    default Optional<V> optionalGet(String k) {
        return Optional.ofNullable(get(k));
    }

    @Override
    default V get(String k) {
        if (CollUtil.isEmpty(get(Collections.singletonList(k)))) {
            return null;
        }
        return get(Collections.singletonList(k)).get(0);
    }

}
