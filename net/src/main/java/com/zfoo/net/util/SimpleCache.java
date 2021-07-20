package com.zfoo.net.util;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.protocol.model.Pair;
import com.zfoo.scheduler.manager.SchedulerBus;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * caffeine的缓存都是单个存，单个更新，但是实际的项目很多都是批量查询，批量更新。
 * <p>
 * 优势：
 * 1.支持批量查找，批量更新。
 * 2.可以防止缓存穿透，缓存击穿，缓存雪崩
 * <p>
 * 当查找的key不存在的时候，返回defaultValue，并且将defaultValue放入缓存。所以通过
 *
 * @author islandempty
 * @since 2021/7/18
 **/
public class SimpleCache<K, V> {

    /**
     * 每次最多更新1000条数据
     */
    private static final int BATCH_RELOAD_SIZE = 1000;

    private LoadingCache<K,V> cache;
    private ConcurrentLinkedQueue<K> linkedQueue;

    private long expiredAccessDuration;
    private long refreshDuration;
    private Function<List<K>, List<Pair<K, V>>> batchReloadCallback;
    private Function<K, V> defaultValueBuilder;

    /**
     * @param expiredAccessDuration 访问过期时间，毫秒；通常情况下，这个值比refreshDuration大会得到更好的缓存效果，一般是2倍
     * @param refreshDuration       刷新实际毫秒；因为是通过后台scheduler去更新缓存，所以更新的refresh可能是这个值的2倍
     * @param maxSize               缓存大小
     * @param batchReloadCallback   一组key取value的回调方法
     * @param defaultValueBuilder   默认值构建
     * @return 简单的缓存
     */
    public static <K,V> SimpleCache<K,V> build(long expiredAccessDuration, long refreshDuration, long maxSize, Function<List<K>,List<Pair<K, V>>> batchReloadCallback
    , Function<K, V> defaultValueBuilder){
        var linkedQueue = new ConcurrentLinkedQueue<K>();

        // 没有用guava的expireAfterWrite的原因是容易造成缓存击穿
        var cache = Caffeine.newBuilder()
                .expireAfterAccess(expiredAccessDuration, TimeUnit.MILLISECONDS)
                .refreshAfterWrite(refreshDuration, TimeUnit.MILLISECONDS)
                .maximumSize(maxSize)
                .recordStats()
                .build(new CacheLoader<K, V>() {
                    @Override
                    public @Nullable V load(@NonNull K k) throws Exception {
                        // 因为通过SimpleCache封装过后，上层逻辑是不会调用guava的cache的get方法，所以理论上load不会执行
                        var result = batchReloadCallback.apply(List.of(k));
                        return CollectionUtils.isEmpty(result) ? defaultValueBuilder.apply(k) : result.get(0).getValue();
                    }

                    @Override
                    public @Nullable V reload(@NonNull K key, @NonNull V oldValue) throws Exception {
                        linkedQueue.offer(key);
                        // 先返回老的值，等周期任务刷新新的值
                        return oldValue;
                    }
                });

        SchedulerBus.schedulerAtFixedRate(new Runnable() {
            @Override
            public void run() {
                var list = new ArrayList<K>();
                while (!linkedQueue.isEmpty()){
                    K key = linkedQueue.poll();
                    list.add(key);
                    if (list.size()>BATCH_RELOAD_SIZE){
                        var result = batchReloadCallback.apply(list);
                        result.forEach(it -> cache.put(it.getKey(), it.getValue()));
                        list.clear();
                    }
                }
                //如果list没有1000个缓存
                if (CollectionUtils.isNotEmpty(list)){
                    var result = batchReloadCallback.apply(list);
                    result.forEach(it -> cache.put(it.getKey(), it.getValue()));
                }
            }
        },refreshDuration, TimeUnit.MILLISECONDS);

        var simpleCache = new SimpleCache<K, V>();
        simpleCache.cache = cache;
        simpleCache.linkedQueue = linkedQueue;
        simpleCache.expiredAccessDuration = expiredAccessDuration;
        simpleCache.refreshDuration = refreshDuration;
        simpleCache.batchReloadCallback = batchReloadCallback;
        simpleCache.defaultValueBuilder = defaultValueBuilder;
        return simpleCache;
    }

    /**
     * 单个查找
     */
    public V get(K k){
        try {
            return cache.get(k);
        }catch (Exception e){
            var apply = defaultValueBuilder.apply(k);
            cache.put(k ,apply);
            return apply;
        }
    }



    /**
     * 异步刷新缓存
     */
    public void invalidate(K k){
        cache.invalidate(k);
    }

    public void put(K k, V v){
        cache.put(k,v);
    }
    /**
     * 批量查找
     */
    public Map<K ,V> batchGet(Collection<K> list){
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        var result = new HashMap<K, V>();

        var notPresentIdSet = new HashSet<K>();
        for (var id : list) {
            var value = cache.getIfPresent(id);
            if (value != null) {
                result.put(id, value);
            } else {
                notPresentIdSet.add(id);
            }
        }

        if (CollectionUtils.isNotEmpty(notPresentIdSet)) {
            batchReloadCallback.apply(new ArrayList<>(notPresentIdSet))
                    .forEach(it -> {
                        result.put(it.getKey(), it.getValue());
                        cache.put(it.getKey(), it.getValue());
                        notPresentIdSet.remove(it.getKey());
                    });
        }

        // 防止缓存穿透，将数据库查询不到的键存入到缓存，value给一个默认值
        if (CollectionUtils.isNotEmpty(notPresentIdSet)) {
            notPresentIdSet.forEach(it -> {
                var defaultValue = defaultValueBuilder.apply(it);
                result.put(it, defaultValue);
                cache.put(it, defaultValue);
            });
        }

        return result;
    }

}

