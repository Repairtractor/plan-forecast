package com.yt.plan.forecast.cache;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Component
@EnableConfigurationProperties({RedisCacheConstant.class})
public class RedisLockComponent {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    public RedisCacheConstant redisCacheConstant;


    private final ThreadLocal<Queue<Lock>> locks = ThreadLocal.withInitial(ArrayDeque::new);


    public void lock(String key, int lockTime) {
        RLock lock = redissonClient.getLock(key);
        locks.get().add(lock);
        lock.lock();
    }

    public void unlock() {
        Lock lock = locks.get().poll();
        if (lock != null) {
            lock.unlock();
        }
    }

    public boolean isLocked(String key) {
        return redissonClient.getLock(key).isLocked();
    }

    public void multiLock(Collection<String> keys, int lockTime) {
        RLock[] rLocks = keys.stream().distinct().parallel().map(redissonClient::getLock).toArray(RLock[]::new);
        RLock multiLock = redissonClient.getMultiLock(rLocks);
        locks.get().add(multiLock);
        multiLock.lock(lockTime, TimeUnit.SECONDS);
    }

}
