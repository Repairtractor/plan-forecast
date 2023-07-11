//package com.yt.plan.forecast.threadlocal;
//
//import com.alibaba.ttl.TransmittableThreadLocal;
//import com.yt.bi.operation.component.forecast.business.SyncContext;
//
//public final class SyncContextThreadLocal {
//    private static final TransmittableThreadLocal<SyncContext> THREAD_LOCAL = new TransmittableThreadLocal<>();
//
//    public static void set(SyncContext syncContext) {
//        THREAD_LOCAL.set(syncContext);
//        ThreadLocalManger.set(THREAD_LOCAL);
//    }
//
//    public static SyncContext get() {
//
//        SyncContext syncContext = THREAD_LOCAL.get();
//        if (syncContext == null) {
//            THREAD_LOCAL.set(new SyncContext());
//        }
//        return THREAD_LOCAL.get();
//    }
//
//    public static void remove() {
//        THREAD_LOCAL.remove();
//    }
//
//}
