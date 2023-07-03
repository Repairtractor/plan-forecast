package com.yt.plan.forecast.cache;

import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
   public  class TestCache extends AbstractRedisCache<String, String> {


        public TestCache() {
            super("test", new GetSetter<>(String::toString, String::toString));
        }

        @Override
        public List<String> sourceAll(Collection<String> keys) {
            //生成测试数据返回
            return Lists.list("1", "2", "3");
        }

    }