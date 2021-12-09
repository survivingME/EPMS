package com.qyn.project.util;

import com.qyn.project.entity.Production;

public class JedisOps {
    public static void setObject(String key, Object object) {
        JedisUtil.getJedis().set(key.getBytes(), SerializeUtil.serialize(object));
    }

    public static Object getObject(String key) {
        byte[] bytes = JedisUtil.getJedis().get(key.getBytes());
        return SerializeUtil.deserialize(bytes);
    }

    public static void saveProduction(Production production) {
        StringBuffer sb = new StringBuffer();
        String productionKey = sb.append(production.getCompanyCode()).append(production.getNameCode()).append(production.getSerial()).toString();
        JedisUtil.getJedis().rpush(Code.REDIS_PRODUCTION_LIST, productionKey);
        setObject(productionKey, production);
    }
}
