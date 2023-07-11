//package com.yt.plan.forecast.mp;
//
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import lombok.SneakyThrows;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationHandler;
//
//public class BaseDoService<M extends BaseMapper<P>, P> extends ServiceImpl<M, P> {
//    public BaseDoService(M baseMapper) {
//        this.baseMapper = baseMapper;
//        this.mapperClass = currentMapperClass();
//    }
//
//    public BaseDoService(M baseMapper, Class<P> entityClass) {
//        this.baseMapper = baseMapper;
//        this.entityClass = entityClass;
//        //mapperClass在初始化的构造函数中重新赋值
//        this.mapperClass = currentMapperClass();
//
//    }
//
//    /**
//     * 获取当前mapper的class
//     *
//     * @return
//     */
//    @SneakyThrows
//    @Override
//    protected Class currentMapperClass() {
//        Class pClass = super.currentMapperClass();
//
////        if (pClass.equals(Object.class) && baseMapper != null) {
////            InvocationHandler invocationHandler = Proxy.getInvocationHandler(baseMapper);
////            Object advised = ReflectUtil.getFieldValue(invocationHandler, "advised");
////            ProxyFactory proxyFactory = (ProxyFactory) advised;
////            Object mybatisMapperProxy = ReflectUtil.getFieldValue(proxyFactory.getTargetSource().getTarget(), "h");
////            return ClassUtils.getUserClass(baseMapper);
////        }
////        return Object.class;
//        //判断当前的pClass是否为Object.class
//        if ((pClass==null||Object.class.equals(pClass)) && baseMapper != null) {
//            BaseMapper mp = this.baseMapper;
//            Field h = mp.getClass().getSuperclass().getDeclaredField("h");
//            h.setAccessible(true);
//            InvocationHandler mapperProxy = (InvocationHandler) h.get(mp);
//            Field mapperInterface = mapperProxy.getClass().getDeclaredField("mapperInterface");
//            mapperInterface.setAccessible(true);
//            return (Class) mapperInterface.get(mapperProxy);
//        }
//        return Object.class;
//    }
//
//    @Override
//    protected Class<P> currentModelClass() {
//        return super.currentModelClass();
//    }
//}
