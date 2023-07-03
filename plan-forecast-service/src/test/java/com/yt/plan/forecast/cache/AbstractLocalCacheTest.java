package com.yt.plan.forecast.cache;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;


@SpringBootTest(classes = {TestApplication.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class AbstractLocalCacheTest {


    TestCache testCache;

    /**
     * 测试redisson的timeout bug
     */
    @Test
    public void testCache() {
        //生成一千个长度为64的字符串集合
        List<String> keys = IntStream.range(0, 1000)
                .mapToObj(i -> RandomUtil.randomString(255))
                .collect(toList());


        testCache.put(keys);
    }

    @Before
    public void init() {
        testCache = SpringUtil.getBean(TestCache.class);
    }


}