package com.ie.util.security;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author islandempty
 * @since 2021/6/30
 **/
public abstract class IdUtils {
        private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);
    /**
     * 本地int的id，如果达到最大值则重新从最小值重新计算
     */
    public static int getLocalIntId() {
        return ATOMIC_INTEGER.incrementAndGet();
    }

    /**
     * 获得分布式环境下唯一id
     *
     * @return String UUID
     */
     public static String getUUID(){
         String uuid = UUID.randomUUID().toString();
         //去掉"-"号
         return uuid.replaceAll("-","");
     }
    /**
     * 小的id在前 - 大的id在后
     *
     * @param a 第一个数字
     * @param b 第二个数字
     * @return 生成的id
     */
    public static String generateStringId(long a, long b) {
        return Math.min(a, b) + "-" + Math.max(a, b);
    }

}

