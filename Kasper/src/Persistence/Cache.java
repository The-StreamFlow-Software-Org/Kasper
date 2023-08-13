package Persistence;

import com.kasper.commons.datastructures.KasperList;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.datastructures.KasperObject;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class Cache {
    private static com.github.benmanes.caffeine.cache.Cache<String, KasperObject> cache;

    public static com.github.benmanes.caffeine.cache.Cache<String, KasperObject> cache() {
        return cache;
    }
    private static com.github.benmanes.caffeine.cache.Cache<KasperObject, ConcurrentLinkedDeque<String>> objectSet;

    public static void init () {
        /*
        cache = Caffeine.newBuilder()
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();
        objectSet = Caffeine.newBuilder()
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .maximumSize(100)
                .build(); */
    }

    public static void set (String path, KasperObject value) {
        if (true) return;
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
        cache().invalidateAll();
        objectSet.invalidateAll();
        //TODO: fix cache invalidation algorithm.
        // For now, let's settle with invalidateAll()
        if (false) {
            var pathList = objectSet.getIfPresent(object);
            if (pathList != null) {
                for (var x : pathList) {
                    cache.invalidate(x);
                }
                objectSet.invalidate(object);
            }
            invalidateChildren(object);
        }
    }

    protected static void invalidateChildren (KasperObject o) {
        System.out.println("Invalidating children...");
        if (o instanceof KasperMap) {
            for (var x : o.toMap().entrySet()) {
                invalidateObject(x.getValue());
            }
            o.toMap().clear();
        } else if (o instanceof KasperList) {
            int index = 0;
            for (var x : o.toList()) {
                invalidateObject(x);
            } o.toList().clear();
        }
    }

    public static KasperObject get (String path) {
        if (true) return null;
        return cache.getIfPresent(path);
    }


}
