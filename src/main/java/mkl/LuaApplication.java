package mkl;

import mkl.config.RedisConfig;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mkl
 * @date 2020/3/30 8:30
 * @description
 */
public class LuaApplication {
    private final static String lualock = "local key     = KEYS[1]"+ "\n" +
            "local content = ARGV[1]"+ "\n" +
            "local ttl     = tonumber(ARGV[2])"+ "\n" +
            "local lockSet = redis.call('setnx', key, content)"+ "\n" +
            "if lockSet == 1 then"+ "\n" +
            "redis.call('PEXPIRE', key, ttl)"+ "\n" +
            "else"+ "\n" +
            "local value = redis.call('get', key)"+ "\n" +
            "if(value == content) then"+ "\n" +
            "lockSet = 1;"+ "\n" +
            "redis.call('PEXPIRE', key, ttl)"+"\n"+
            "end"+"\n"+
            "end"+"\n"+
            "return lockSet";

    private final static String luaUnlock = "local key     = KEYS[1]"+ "\n" +
            "local content = ARGV[1]"+ "\n" +
            "local value = redis.call('get', key)"+ "\n" +
            "if value == content then"+ "\n" +
            "return redis.call('del', key)"+ "\n" +
            "return 'this bloomkey is null'"+ "\n" +
            "else"+ "\n" +
            "return 0"+ "\n" +
            "end";
    /**
     * 测试添加字符串至布隆过滤器
     */
    public static void main(String[] args) {
        Jedis jedis = RedisConfig.getJedis();
        List<String> keys = new ArrayList<>();
        keys.add("mkllock");
        keys.add("");
        List<String> arggs = new ArrayList<>();
        arggs.add("2");
        arggs.add("8000");
        String luaLoad = jedis.scriptLoad(lualock);
        System.out.println(luaLoad);
        Object obj = jedis.evalsha(luaLoad,keys,arggs);
        System.out.println(obj);
    }

    /**
     * 测试查询字符串是否存在
     */
//    public static void main(String[] args) {
//        Jedis jedis = RedisConfig.getJedis();
//        List<String> keys = new ArrayList<>();
//        keys.add("mkllock");
//        List<String> arggs = new ArrayList<>();
//        arggs.add("2");
//        String luaLoad = jedis.scriptLoad(luaUnlock);
//        System.out.println(luaLoad);
//        Object obj = jedis.evalsha(luaLoad,keys,arggs);
//        System.out.println(obj);
//    }

}
