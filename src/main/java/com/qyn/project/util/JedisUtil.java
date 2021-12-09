package com.qyn.project.util;

import redis.clients.jedis.Jedis;

public class JedisUtil {
    private volatile static Jedis jedis;

    private JedisUtil() {}

    public static Jedis getJedis() {
        if(jedis == null) {
            synchronized (JedisUtil.class) {
                if(jedis == null) jedis = new Jedis("localhost");
            }
        }
        return jedis;
    }

    public static void bgSave() {
        jedis.bgsave();
    }

}
