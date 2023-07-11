//package com.yt.plan.forecast.threadlocal;
//
//import com.alibaba.ttl.TransmittableThreadLocal;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public final class ThreadLocalManger {
//
//    private static final ThreadLocal<Set<ThreadLocal<?>>> removeBle=new TransmittableThreadLocal<>();
//
//    public static void set(ThreadLocal<?> threadLocal) {
//        Set<ThreadLocal<?>> threadLocalSet = removeBle.get();
//        if (threadLocalSet == null) {
//            threadLocalSet = new HashSet<>();
//            removeBle.set(threadLocalSet);
//        }
//        threadLocalSet.add(threadLocal);
//    }
//
//    public static void remove() {
//        Set<ThreadLocal<?>> threadLocalSet = removeBle.get();
//        if (threadLocalSet != null) {
//            threadLocalSet.forEach(ThreadLocal::remove);
//        }
//        removeBle.remove();
//    }
//
//    public static void remove(ThreadLocal<?> local){
//        local.remove();
//    }
//
//}
