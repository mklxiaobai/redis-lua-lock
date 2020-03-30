package mkl.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author mkl
 * @date 2020/3/30 8:29
 * @description
 */
public class RedisConfig {
    private static JedisPool jedisPool;

    static{
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);
        jedisPool = new JedisPool(jedisPoolConfig,"127.0.0.1",6379,1000,"123456");
    }

    public static Jedis getJedis(){
        if(null != jedisPool){
            return jedisPool.getResource();
        }
        throw new RuntimeException("JedisPool was not init!");
    }
}
