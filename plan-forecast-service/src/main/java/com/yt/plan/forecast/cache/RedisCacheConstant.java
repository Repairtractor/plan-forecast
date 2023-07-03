package com.yt.plan.forecast.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "redis.cache")
public class RedisCacheConstant {

    //redisson 锁的基础时间
    public final Integer baseLockTime = 40;

    //redisson 锁的增加时间
    public final Integer bound = 20;


    //是否分布式部署
    public boolean distributed = false;

    //redisson配置文件路径
    public String PATH = "cc:cache:";

    //redisson锁路径
    public String LOCK_PATH = "cc:lock:";

    /**
     * redis stream name
     */
    public String streamName = "cc:stream";

    /**
     * redis stream consumer group
     */
    public String redisStreamConsumerGroup = "redisCacheRetryGroup";

    /**
     * redis stream consumer name
     */
    public String consumerName = "redisCacheRetryConsumer";


    /**
     * 队列中的重试次数
     */
    public int retryTime = 2;

    public int failMaxSize = 500;


}