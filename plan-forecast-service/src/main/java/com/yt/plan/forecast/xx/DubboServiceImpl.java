package com.yt.plan.forecast.xx;

import com.plan.forecast.dubbo.DubboService;

public class DubboServiceImpl implements DubboService {

    @Override
    public void test(String name) {
        System.out.println("DubboServiceImpl.test");
        System.out.println("你成功了=======================================");
    }
}
