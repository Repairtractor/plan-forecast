//package com.yt.plan.forecast.threadlocal;
//
//import com.alibaba.ttl.TransmittableThreadLocal;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public final class SkuSaleNumbsYearsThreadLocal {
//    private static final TransmittableThreadLocal<Set<String>> THREAD_LOCAL = new TransmittableThreadLocal<>();
//
//
//    public static void set(Set<String> forecastSkuCodeSet) {
//        THREAD_LOCAL.set(forecastSkuCodeSet);
//        ThreadLocalManger.set(THREAD_LOCAL);
//    }
//
//    public static Set<String> get() {
//        Set<String> codeSet = THREAD_LOCAL.get();
//        if (codeSet == null) {
//            THREAD_LOCAL.set(new HashSet<>(16));
//        }
//        return THREAD_LOCAL.get();
//    }
//
//    public static void remove() {
//        THREAD_LOCAL.remove();
//    }
//
//
//
//}
