package lxk.util;

/**
 * @author 刘雄康
 * @version v1.0
 * @description Test
 * @date 2018年12月05日
 */
public class Test {
    public static void main(String[] args) {
        RedissonManager.init(); //初始化

        testConcurrentThreadGetLock();


    }

    private static void testConcurrentThreadGetLock() {
        for (int i = 0; i < 1; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String key = "test123";
                        DistributedRedisLock.acquire(key);
                        Thread.sleep(1000); //获得锁之后可以进行相应的处理
                        System.err.println("======获得锁后进行相应的操作======");
                        DistributedRedisLock.release(key);
                        System.err.println("=============================");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }
}
