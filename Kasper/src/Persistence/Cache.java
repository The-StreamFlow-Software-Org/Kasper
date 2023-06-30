package Persistence;

import KasperCommons.DataStructures.KasperObject;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class Cache {
    private static com.github.benmanes.caffeine.cache.Cache<String, KasperObject> cache;
    private static com.github.benmanes.caffeine.cache.Cache<KasperObject, String> cacheAsPath;

    public static com.github.benmanes.caffeine.cache.Cache<String, KasperObject> cache() {
        return cache;
    }

    public static void init () {
        cache = Caffeine.newBuilder()
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();
        cacheAsPath = Caffeine.newBuilder()
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();
    }

    public static void set (String path, KasperObject value) {
        cache.put(path, value);
        cacheAsPath.put(value, path);
    }

    public static void invalidateObject (KasperObject object) {
        var cached = cacheAsPath.getIfPresent(object);
        if (cached == null) return;
        cacheAsPath.invalidate(object);
        cache.invalidate(cached);
    }

    public static KasperObject get (String path) {
        return cache.getIfPresent(path);
    }


}
