package Persistence;

import KasperCommons.DataStructures.KasperObject;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class Cache {
    private static com.github.benmanes.caffeine.cache.Cache<String, KasperObject> cache;

    public static com.github.benmanes.caffeine.cache.Cache<String, KasperObject> cache() {
        return cache;
    }
    private static com.github.benmanes.caffeine.cache.Cache<KasperObject, ConcurrentLinkedDeque<String>> objectSet;

    public static void init () {
        cache = Caffeine.newBuilder()
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();
        objectSet = Caffeine.newBuilder()
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();
    }

    public static void set (String path, KasperObject value) {
        cache.put(path, value);
        var list = objectSet.getIfPresent(value);
        if (list!=null) list.add(path);
        else {
            var arrayList = new ConcurrentLinkedDeque<String>();
            arrayList.add(path);
            objectSet.put(value, arrayList);
        }
    }

    public static void invalidateObject (KasperObject object) {
        var pathList = objectSet.getIfPresent(object);
        if (pathList == null) return;
        for (var x : pathList) {
            cache.invalidate(x);
        } objectSet.invalidate(object);
    }

    public static KasperObject get (String path) {
        return cache.getIfPresent(path);
    }


}
