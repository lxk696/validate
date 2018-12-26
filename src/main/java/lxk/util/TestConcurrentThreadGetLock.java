package lxk.util;

import com.alibaba.fastjson.JSON;
import lxk.model.Book;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RFuture;
import org.redisson.api.RMap;

import java.util.List;
import java.util.concurrent.Future;

/**@link https://www.cnblogs.com/tietazhan/p/6307304.html
 * @author 刘雄康
 * @version v1.0
 * @description Test
 * @date 2018年12月05日
 */
public class TestConcurrentThreadGetLock {

    public static void main(String[] args) {

        Redisson redisson =null;
        try {
            RedissonManager.init(); //初始化
            redisson = RedissonManager.getRedisson();
            //testConcurrentThreadGetLock();
            //testList(redisson);
            testAnyBody(redisson);
            //testAnyBodyStr(redisson);
            //testMap(redisson);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null!=redisson){
                redisson.shutdown();
            }
        }
    }

    private static void testMap(Redisson redisson) throws InterruptedException, java.util.concurrent.ExecutionException {
        System.out.println("---------------testMap--------------------");
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
        System.out.println("-------------testAnyBody----------");
        RBucket<Book> bucket = redisson.getBucket("anyObject");
        System.out.println(bucket.get());
        System.out.println(bucket.trySet(new Book(3)));
        System.out.println(bucket.compareAndSet(new Book(3), new Book(5)));
        System.out.println(bucket.getAndSet(new Book(11)));
        System.out.println(bucket.get());
        System.out.println(bucket.delete());
        System.out.println(bucket.get());
    }

    private static void testAnyBodyStr(Redisson redisson) {
        System.out.println("-------------testAnyBodyStr----------");
        RBucket<String> bucket = redisson.getBucket("anyObjecStr");
        System.out.println(bucket.get());
        System.out.println(bucket.trySet("anyObjecStr2222"));
        System.out.println(bucket.delete());
        System.out.println(bucket.get());

        //RBucket<String> bucket2 = redisson.getBucket("anyObjecStr");
        //System.out.println(bucket2.get());
    }

    private static void testList(Redisson redisson) {
        System.out.println("---------------testList--------------------");
        List<Integer> list = redisson.getList("list");
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        System.out.println("list:" + redisson.getList("list"));

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
