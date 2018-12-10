package lxk.util;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.config.Config;

public class RedissonManager {

    private static final String RAtomicName = "genId_";

    private static Config config = new Config();
    private static Redisson redisson = null;

    public static void init(){

        try {
            config.useClusterServers() //这是用的集群server
                    .setScanInterval(20000) //设置集群状态扫描时间
                    .setMasterConnectionPoolSize(1000) //设置连接数
                    .setSlaveConnectionPoolSize(1000)
                    // .setPassword("baobeituan")
                    // .addNodeAddress("http://39.108.244.68:6379", "http://39.108.244.68:6380", "http://39.108.244.68:6381", "http://39.108.244.68:7379", "http://39.108.244.68:7380", "http://39.108.244.68:7381");
                    .setPassword("lxk123pwd")
                    .addNodeAddress("http://112.74.182.19:6379", "http://112.74.182.19:6380", "http://112.74.182.19:6381", "http://112.74.182.19:7379", "http://112.74.182.19:7380", "http://112.74.182.19:7381");
        redisson = (Redisson) Redisson.create(config);
            //清空自增的ID数字
            RAtomicLong atomicLong = redisson.getAtomicLong(RAtomicName);
            atomicLong.set(1);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public static Redisson getRedisson(){
        return redisson;
    }

    /** 获取redis中的原子ID */
    public static Long nextID(){
        RAtomicLong atomicLong = getRedisson().getAtomicLong(RAtomicName);
        atomicLong.incrementAndGet();
        return atomicLong.get();
    }
}
