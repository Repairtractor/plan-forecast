package com.yt.plan.forecast.cache;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ConcurrencyUtil {


    /**
     * 初始化线程
     * 100个核心线程，200个最大线程数，核心线程之外的空闲线程最大存活时间为1分钟，任务会放入缓存队列中
     */
    private static final Executor pool = new ThreadPoolExecutor(100, 200, 60,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    private ConcurrencyUtil() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static Executor getPool(){
        return pool;
    }

    public static void execute(Runnable runnable) {
        pool.execute(runnable);
    }


}
