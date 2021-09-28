package com.zfoo.net.util;

import com.ie.util.ThreadUtils;
import com.zfoo.protocol.model.Pair;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;

/**
 * @author islandempty
 * @since 2021/7/27
 **/

@Ignore
public class SimpleCacheTest {

    @Test
    public void test(){
        var cache = SimpleCache.build(3000, 6000, 100, new Function<List<String>, List<Pair<String, String>>>() {
            @Override
            public List<Pair<String, String>> apply(List<String> objects) {
                return null;
            }
        }, key -> "empty");

        cache.put("a", "b");
        cache.get("a");
        ThreadUtils.sleep(3000);
        System.out.println(cache.get("a"));
        ThreadUtils.sleep(1000);
        System.out.println(cache.get("a"));
        ThreadUtils.sleep(1000);
        System.out.println(cache.get("a"));
        ThreadUtils.sleep(1000);
        System.out.println(cache.get("a"));
        ThreadUtils.sleep(1000);
        System.out.println(cache.get("a"));

        ThreadUtils.sleep(Long.MAX_VALUE);
    }
}

