package Persistence;

import KasperCommons.DataStructures.KasperObject;
import Server.SuperClass.KasperGlobalMap;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.checkerframework.checker.units.qual.C;

import java.util.concurrent.TimeUnit;

public class Cache {
    private static com.github.benmanes.caffeine.cache.Cache<String, KasperObject> cache;

    public static com.github.benmanes.caffeine.cache.Cache<String, KasperObject> cache() {
        return cache;
    }

    public static void init () {
        cache = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }

    public static void set (String path, KasperObject value) {
        cache.put(path, value);
    }

    public static KasperObject get (String path) {
        return cache.getIfPresent(path);
    }

}
