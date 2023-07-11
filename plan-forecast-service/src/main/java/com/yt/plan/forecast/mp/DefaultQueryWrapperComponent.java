//package com.yt.plan.forecast.mp;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.StrUtil;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//
//import java.util.Arrays;
//import java.util.Map;
//import java.util.Objects;
//
//public class DefaultQueryWrapperComponent<T> {
//
//    private Class<T> clazz;
//    public DefaultQueryWrapperComponent(Class<T> clazz) {
//        this.clazz = clazz;
//    }
//
//    public QueryWrapper<T> getWrapper(Map<String, Object> params) {
//        QueryWrapper<T> wrapper = new QueryWrapper<>();
//        //如果params为空 wrapper查询全部
//        if (CollUtil.isEmpty(params)){
//            return wrapper;
//        }
//
//        params.forEach((key, value) -> {
//            //如果后缀为eq
//            if (key.endsWith("eq")) {
//                wrapper.eq(StrUtil.toSymbolCase(key.substring(0, key.length() - 3),'_'), value);
//            } else if (key.endsWith("in")) {
//                wrapper.in(StrUtil.toSymbolCase(key.substring(0, key.length() - 3),'_')  , Objects.toString(value).split(","));
//            }
//        });
//
//        wrapper.orderByDesc("create_time");
//
//        //如果有columns
//        if (params.containsKey("columns")) {
//            wrapper.select(Arrays.stream(params.get("columns").toString().split(",")).map(it -> StrUtil.toSymbolCase(it, '_')).toArray(String[]::new));
//        }
//        return wrapper;
//    }
//
//}
