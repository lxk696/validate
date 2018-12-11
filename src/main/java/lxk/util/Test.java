package lxk.util;

import com.alibaba.fastjson.JSON;
import lxk.model.Book;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RFuture;
import org.redisson.api.RMap;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

/**
 * @author 刘雄康
 * @version v1.0
 * @description Test
 * @date 2018年12月05日
 */
public class Test {

    public static void main(String[] args) {
        try {
            RedissonManager.init(); //初始化
            Redisson redisson = RedissonManager.getRedisson();
            //testConcurrentThreadGetLock();
            //testList(redisson);
            //testAnyBody(redisson);
            testMap(redisson);

            redisson.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testMap(Redisson redisson) throws InterruptedException, java.util.concurrent.ExecutionException {
        RMap<String, Book> map = redisson.getMap("anyMap");
        System.out.println("1" + JSON.toJSON(map));

        Book prevObject = map.put("123", new Book(123));
        System.out.println("2" + prevObject);
        Book currentObject = map.putIfAbsent("323", new Book(323));
        System.out.println("3" + currentObject);
        Book obj = map.remove("123");
        System.out.println("4" + obj);

        System.out.println("5" + map.fastPut("321", new Book(321)));
        System.out.println("6" + map.fastRemove("321"));

        Future<Book> putAsyncFuture = map.putAsync("32111", new Book(32111));
        System.out.println("7" + putAsyncFuture.get());
        RFuture<Boolean> fastPutAsyncFuture = map.fastPutAsync("321000", new Book(321000));
        System.out.println("8" + fastPutAsyncFuture.get());
        System.out.println("9" + JSON.toJSON(map.fastPutAsync("321000", new Book(321000))));
        System.out.println("10" + JSON.toJSON(map.fastRemoveAsync("321")));
    }

    private static void testAnyBody(Redisson redisson) {
        RBucket<Book> bucket = redisson.getBucket("anyObject");
        System.out.println(bucket.get());
        System.out.println(bucket.trySet(new Book(3)));
        System.out.println(bucket.compareAndSet(new Book(4), new Book(5)));
        System.out.println(bucket.getAndSet(new Book(new Random().nextInt(100))));
    }

    private static void testList(Redisson redisson) {
        List<Integer> list = redisson.getList("list");
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        System.out.println("list:" + redisson.getList("list"));
        System.out.println("-----------------------------------");
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
