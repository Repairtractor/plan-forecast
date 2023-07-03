package com.yt.plan.forecast.cache;

import lombok.AllArgsConstructor;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class CacheConstant {
    public final int maxSize;
    public final int expireTime;

    public final TimeUnit timeUnit;
}
