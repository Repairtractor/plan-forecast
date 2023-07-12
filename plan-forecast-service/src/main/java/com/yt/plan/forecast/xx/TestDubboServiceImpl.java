package com.yt.plan.forecast.xx;

import com.plan.forecast.dubbo.TestDubboService;
import org.apache.dubbo.config.annotation.DubboService;


@DubboService
public class TestDubboServiceImpl implements TestDubboService {

    @Override
    public void test(String name) {
        System.out.println("DubboServiceImpl.test");
        System.out.println("你成功了=======================================");
    }
}
