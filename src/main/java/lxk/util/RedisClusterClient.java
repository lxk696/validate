package lxk.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.*;

import java.util.*;

public class RedisClusterClient {

  private static Log LOG = LogFactory.getLog(RedisClusterClient.class);

  private static List<String> redisHosts = null;

  private static RedisClusterClient redisClient = null;

  private static JedisCluster jedisCluster = null;

  private RedisClusterClient() {
    ;
  }

  public static void main(String[] args) {
      try {
          RedisClusterClient clusterInstance = RedisClusterClient.getClusterInstance();
          System.out.println("get nullkey: " + clusterInstance.get("nullkey"));
          System.out.println("get T1:                " + clusterInstance.get("T1"));
          System.out.println("get test2018121011111: " + clusterInstance.get("test2018121011111"));
          //System.out.println("set test2018121011111 :" + clusterInstance.set("test2018121011111", "test2018121011111"));
          System.out.println("get test201812101111:  " + clusterInstance.get("test201812101111"));
          System.out.println("T1".equals("T1"));
          System.out.println("test2018121011111".equals("test2018121011111"));
          jedisCluster.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  private static void initRedisCluster() {
    try {
      if (redisHosts != null && !redisHosts.isEmpty()) {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        String[] hostMeta = null;
        for (String host : redisHosts) {
          hostMeta = host.split(":");
          jedisClusterNodes.add(new HostAndPort(hostMeta[0], Integer.parseInt(hostMeta[1])));
          LOG.info("host=" + host);
        }

        JedisPoolConfig config = new JedisPoolConfig();
          // 最大连接数
        config.setMaxTotal(200);
          // 最大空闲连接数
        config.setMaxIdle(50);
          // 设置最小空闲数
          config.setMinIdle(8);
          //获取连接时的最大等待毫秒数,小于零:阻塞不确定的时间,默认-1
          config.setMaxWaitMillis(3000);
          //在获取连接的时候检查有效性, 默认false
        config.setTestOnBorrow(true);
          //在归还给pool时，是否提前进行validate操作
        config.setTestOnReturn(true);
          //在空闲时检查有效性, 默认false
          config.setTestWhileIdle(true);
          //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
          config.setBlockWhenExhausted(true);
          //连接空闲多久后释放, 当空闲时间>该值 且 空闲连接>最大空闲连接数 时直接释放
          config.setSoftMinEvictableIdleTimeMillis(100);
        // Idle时进行连接扫描
        config.setTestWhileIdle(true);
          // 表示idle object evitor两次扫描之间要sleep的毫秒数----释放连接的扫描间隔（毫秒）
        config.setTimeBetweenEvictionRunsMillis(30000);
          // 表示idle object evitor每次扫描的最多的对象数 -----每次释放连接的最大数目
        config.setNumTestsPerEvictionRun(10);
          // ------连接最小空闲时间
          // 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
        config.setMinEvictableIdleTimeMillis(60000);


          jedisCluster =
            new JedisCluster(
                    //jedisClusterNodes, 3000, 3000, 5, "baobeituan", config); // 如果为测试ip则使用密码登录
                    // jedisClusterNodes, config); // 如果为测试ip则使用密码登录
                    jedisClusterNodes, 3000, 3000, 2, "lxk123pwd", config);

      } else {
        LOG.error("redisHosts is not config!");
        throw new RuntimeException("redisHosts is not config!");
      }
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

  public static final RedisClusterClient getClusterInstance() {
    try {
      if (null == redisClient) {
        synchronized (RedisClusterClient.class) {
          if (null == redisClient) {
              redisHosts =
                      Arrays.asList("112.74.182.19:6379,112.74.182.19:6380,112.74.182.19:6381,112.74.182.19:7379,112.74.182.19:7380,112.74.182.19:7381".split(","));
              //Arrays.asList("39.108.244.68:6379,39.108.244.68:6380,39.108.244.68:6381,39.108.244.68:7379,39.108.244.68:7380,39.108.244.68:7381".split(","));
              initRedisCluster();
            redisClient = new RedisClusterClient();
          }
        }
      }

    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
    return redisClient;
  }

  public Long customGroupSadd(final Long groupId, final String setKey, final List<String> values) {
    Long res = null;
    try {
      res = customGroupSadd(groupId, setKey, values.toArray(new String[values.size()]));
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  public Long customGroupSadd(final Long groupId, final String setKey, final String... values) {
    Long res = null;
    try {
      res = jedisCluster.sadd("{customGroup" + groupId + "}".concat(setKey), values);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  public Set<String> customGroupSinter(
      final Long groupId, final String setKeyA, final String setKeyB) {
    Set<String> res = null;
    try {
      res =
          jedisCluster.sinter(
              "{customGroup" + groupId + "}".concat(setKeyA),
              "{customGroup" + groupId + "}".concat(setKeyB));
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  /**
   * @param pattern key *lxk*
   * @return 返回 所有匹配 Keys
   */
  public TreeSet<String> keys(String pattern) {
    TreeSet<String> keys = new TreeSet<>();
    Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
    for (String k : clusterNodes.keySet()) {
      JedisPool jp = clusterNodes.get(k);
      Jedis connection = jp.getResource();
      try {
        keys.addAll(connection.keys(pattern));
      } catch (Exception e) {
        LOG.error("Getting keys error: {}", e);
      } finally {
        connection.close(); // 用完close这个链接
      }
    }
    return keys;
  }

  public Long ttl(final String key) {
    Long res = null;
    try {
      res = jedisCluster.ttl(key);
    } catch (Exception e) {;
    }
    return res;
  }

  public String set(final String key, final String value) {
    String res = null;
    try {
      res = jedisCluster.set(key, value);
    } catch (Exception e) {;
        LOG.error(e.getMessage(), e);
    }
    return res;
  }

  public String set(final String key, final Boolean isNeedAppName, final String value) {
    String res = null;
    try {
      if (isNeedAppName) {
        res = jedisCluster.set(key, value);
      } else {
        res = jedisCluster.set(key, value);
      }
    } catch (Exception e) {;
    }
    return res;
  }

  public Long hset(final String key, final String hkey, final String value) {
    Long res = null;
    try {
      res = jedisCluster.hset(key, hkey, value);
    } catch (Exception e) {;
    }
    return res;
  }

  public String hget(final String key, final String hkey) {
    String res = null;
    try {
      res = jedisCluster.hget(key, hkey);
    } catch (Exception e) {;
    }
    return res;
  }

  public Set<String> hkeys(final String key) {
    Set<String> res = null;
    try {
      res = jedisCluster.hkeys(key);
    } catch (Exception e) {;
    }
    return res;
  }

  public Long hdel(final String key, final String hkey) {
    Long res = null;
    try {
      res = jedisCluster.hdel(key, hkey);
    } catch (Exception e) {;
    }
    return res;
  }

  public Long setNx(final String key, final String value) {
    Long res = null;
    try {
      res = jedisCluster.setnx(key, value);
    } catch (Exception e) {;
    }
    return res;
  }

  public String set(
      final String key, final String value, final String nxxx, final String expx, final long time) {
    String res = null;
    try {
        //res = jedisCluster.set(key, value, nxxx, expx, time);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  public String setEx(final String key, final int time, final String value) {
    String res = null;
    try {
      res = jedisCluster.setex(key, time, value);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  /*
   * public String setExNoAppName(final String key, final int time, final String value) { String
   * res = null; try { res = jedisCluster.setex(key, time, value); } catch (Exception e) { ; }
   * return res; }
   */
  public Long expire(final String key, final int seconds) {
    Long res = null;
    try {
      res = jedisCluster.expire(key, seconds);
    } catch (Exception e) {;
    }
      return res;
  }

  public String get(final String key) {
    String res = null;
    try {
      res = jedisCluster.get(key);
    } catch (Exception e) {
        //LOG.error(e.getMessage() + "jedisCluster : " + jedisCluster + " , key  :" + key, e);
    }
    return res;
  }

  public String get(final String key, final Boolean isNeedAppName) {
    String res = null;
    try {
      if (isNeedAppName) {
        res = jedisCluster.get(key);
      } else {
        res = jedisCluster.get(key);
      }
    } catch (Exception e) {;
    }
    return res;
  }

  public List<String> getList(final String key) {
    List<String> res = null;
    try {
      res = jedisCluster.lrange(key, 0, -1);
    } catch (Exception e) {;
    }
    return res;
  }

  // public Object getObject(final String key, Class<?> clazz) {
  //     Object res = null;
  //     String jsonStr = get(key);
  //     if (jsonStr != null && !"".equals(jsonStr)) {
  //         res = new Gson().fromJson(jsonStr, clazz);
  //     }
  //     return res;
  // }

  public Long del(final String key) {
    Long res = null;
    try {
      res = jedisCluster.del(key);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  public Long delKeys(final String... keys) {
    Long res = null;
    try {
      res = jedisCluster.del(keys);
    } catch (Exception e) {;
    }
    return res;
  }

  public Boolean exists(final String key) {
    Boolean res = null;
    try {
      res = jedisCluster.exists(key);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  ///
  public Long rpush(final String key, final String... value) {
    Long res = null;
    try {
      res = jedisCluster.rpush(key, value);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  ////
  public Long rpush(final String key, final Boolean isNeedAppName, final String... value) {
    Long res = null;
    try {
      if (isNeedAppName) {
        res = jedisCluster.rpush(key, value);
      } else {
        res = jedisCluster.rpush(key, value);
      }
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  public Long rpushs(final String key, final Boolean isNeedAppName, List<String> twoqueList) {
    String[] array = twoqueList.toArray(new String[twoqueList.size()]);

    Long res = null;
    try {
      if (isNeedAppName) {
        res = jedisCluster.rpush(key, array);
      } else {
        res = jedisCluster.rpush(key, array);
      }
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  public String rpop(final String key) { // TODO
    String res = null;
    try {
      res = jedisCluster.rpop(key);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  public String brpop(final String key, final int timeout) {
    String res = null;
    try {
      res = jedisCluster.brpop(timeout, key).get(1);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  public Long lpush(final String key, final String... value) {
    Long res = null;
    try {
      res = jedisCluster.lpush(key, value);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  public Long lpush(final String key, final Boolean isNeedAppName, final String... value) {
    Long res = null;
    try {
      if (isNeedAppName) {
        res = jedisCluster.lpush(key, value);
      } else {
        res = jedisCluster.lpush(key, value);
      }
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  public String lpop(final String key) {
    String res = null;
    try {
      res = jedisCluster.lpop(key);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  public String blpop(final String key, final int timeout) {
    String res = null;
    try {
      res = jedisCluster.blpop(timeout, key).get(1);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  public Long llen(final String key) { // TODO
    Long res = null;
    try {
      res = jedisCluster.llen(key);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return res;
  }

  public Long lrem(final String key, final int count, final String value) {
    Long res = null;
    try {
      res = jedisCluster.lrem(key, count, value);
    } catch (Exception e) {;
    }
    return res;
  }

  public Long incrBy(final String key, final long integer) {
    Long res = null;
    try {
      res = jedisCluster.incrBy(key, integer);
    } catch (Exception e) {;
    }
    return res;
  }

  public Long incr(final String key) {
    Long res = null;
    try {
      res = jedisCluster.incr(key);
    } catch (Exception e) {;
    }
    return res;
  }

  public Long decrBy(final String key, final long integer) {
    Long res = null;
    try {
      res = jedisCluster.decrBy(key, integer);
    } catch (Exception e) {;
    }
    return res;
  }

  public Long addSetMember(final String key, final String... member) {
    Long res = null;
    try {
      res = jedisCluster.sadd(key, member);
    } catch (Exception e) {;
    }
    return res;
  }

  public Long delSetMember(final String key, final String... member) {
    Long res = null;
    try {
      res = jedisCluster.srem(key, member);
    } catch (Exception e) {;
    }
    return res;
  }

  public Boolean findSetMember(final String key, final String member) {
    Boolean res = null;
    try {
      res = jedisCluster.sismember(key, member);
    } catch (Exception e) {;
    }
    return res;
  }

  public Object evalScript(final String script, final List<String> keys, final List<String> args) {
    Object res = null;
    try {
      res = jedisCluster.eval(script, keys, args);
    } catch (Exception e) {;
    }
    return res;
  }
}
