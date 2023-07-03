package com.yt.plan.forecast.cache;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * 本地缓存的抽象类
 *
 * @param <K>
 * @param <V>
 * @param <T>
 */
public class DefaultLocalCache<K, V, T> extends AbstractLocalCache<K, V, T> {


    private final Function<Collection<K>, List<T>> allSource;


    public static <K, V, T> Builder<K, V, T> builder(GetSetter<T, K, V> getSetter) {
        return new Builder<>(getSetter);
    }

    public static class Builder<K, V, T> {
        private final GetSetter<T, K, V> getSetter;
        private Function<Collection<K>, List<T>> allSource;

        private Function<K, T> simpleSource;

        private CacheConstant cacheConstant;

        public Builder(GetSetter<T, K, V> getSetter) {
            this.getSetter = getSetter;
        }

        public DefaultLocalCache<K, V, T> build() {
            return new DefaultLocalCache<>(getSetter, allSource, cacheConstant);
        }


        public Builder<K, V, T> allSource(Function<Collection<K>, List<T>> allSource) {
            this.allSource = allSource;
            return this;
        }

        public Builder<K, V, T> simpleSource(Function<K, T> simpleSource) {
            this.simpleSource = simpleSource;
            if (allSource == null) {
                allSource = (keys) -> keys.stream().map(simpleSource).collect(toList());
            }
            return this;
        }


        public Builder<K, V, T> cacheConstant(CacheConstant cacheConstant) {
            this.cacheConstant = cacheConstant;
            return this;
        }
    }


    private DefaultLocalCache(GetSetter<T, K, V> getSetter, Function<Collection<K>, List<T>> allSource, CacheConstant cacheConstant) {
        super(getSetter, cacheConstant);
        this.allSource = allSource;
    }


    @Override
    public List<T> sourceAll(Collection<K> keys) {
        return allSource.apply(keys);
    }
}
