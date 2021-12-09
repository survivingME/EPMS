package com.qyn.project.util;

public class SerialUtil {
    public static String getSerial(String companyCode, String nameCode) {
        StringBuffer sb = new StringBuffer();
        sb.append(companyCode).append(nameCode);
        String serialKey = sb.toString();
        if(JedisUtil.getJedis().exists(serialKey)) {
            return JedisUtil.getJedis().incr(serialKey).toString();
        } else {
            JedisUtil.getJedis().set(serialKey, Code.REDIS_SERIAL_INIT);
            return Code.REDIS_SERIAL_INIT;
        }
    }
}
