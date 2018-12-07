package lxk.util;

import org.redisson.Redisson;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

public class DistributedRedisLock {
    private static Redisson redisson = RedissonManager.getRedisson();
    private static final String LOCK_TITLE = "redisLock_";

    public static void acquire(String lockName){
        String key = LOCK_TITLE + lockName;
        RLock myLock = redisson.getLock(key);
        //lock提供带timeout参数，timeout结束强制解锁，防止死锁
        myLock.lock(2, TimeUnit.MINUTES);

        System.err.println("======lock======"+Thread.currentThread().getName());
    }

    public static void release(String lockName){
        String key = LOCK_TITLE + lockName;
        RLock myLock = redisson.getLock(key);
        myLock.unlock();
        System.err.println("======unlock======"+Thread.currentThread().getName());
    }
}