package com.zfoo.util.math;

/**
 *
 * 改进的hash算法虽然发布比较均匀，但是没有Java自带的hash算法速度快，所以需要知道自己需要什么
 *
 * @author islandempty
 * @since 2021/7/14
 **/
public abstract class HashUtils {

    private static final int P = 16777619;
    private static final int INIT_HASH = (int) 2166136261L;

    /**
     * 改进的32位FNV算法1
     *
     * @param data 数组
     * @return hash结果
     */
    public static int fnvHash(byte[] data) {
        var hash = INIT_HASH;
        for (byte b : data) {
            hash = (hash ^ b) * P;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return Math.abs(hash);
    }

    /**
     * 改进的32位FNV算法1
     *
     * @param object 计算hash的对象，会调用toString方法
     * @return hash结果
     */
    public static int fnvHash(Object object) {
        var hash = object.toString().chars().reduce(INIT_HASH, (left, right) -> (left ^ right) * P);
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return Math.abs(hash);
    }

}

