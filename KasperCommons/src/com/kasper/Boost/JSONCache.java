package com.kasper.Boost;

import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class JSONCache {
    private static com.github.benmanes.caffeine.cache.Cache<Object, String> cache;

    public static com.github.benmanes.caffeine.cache.Cache<Object, String> cache() {
        return cache;
    }

    public static void init () {
        cache = Caffeine.newBuilder()
                .expireAfterAccess(10, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();
    }

    public static void set (Object instance, String value) {
        cache.put(instance, value);
    }

    public static String get (Object path) {
        return cache.getIfPresent(path);
    }

}
