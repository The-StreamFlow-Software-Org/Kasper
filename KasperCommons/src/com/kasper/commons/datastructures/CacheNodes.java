package com.kasper.commons.datastructures;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.w3c.dom.Node;

import java.util.concurrent.TimeUnit;

public class CacheNodes {
    private com.github.benmanes.caffeine.cache.Cache<Object, Node> cache;



    public CacheNodes () {
        this.cache = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }

    public  void set (Object path, Node value) {
        cache.put(path, value);
    }

    public  Node get (Object path) {
        return cache.getIfPresent(path);
    }


}
