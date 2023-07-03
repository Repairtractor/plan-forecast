package com.yt.plan.forecast.cache;

import lombok.AllArgsConstructor;

import java.util.function.Function;

@AllArgsConstructor
public final class GetSetter<T,K,V>{
    public final Function<T, K> keyGetter ;
    public final Function<T, V> valueGetter;
}
