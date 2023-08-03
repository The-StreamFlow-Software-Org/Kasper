package com.kasper.commons.datastructures;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The KasperMap is a class representing the map data structure.
 * It can represent objects.
 */
public class KasperMap extends KasperObject implements Iterable<KasperObject>{

    @Serial
    private static final long serialVersionUID = 3102519298991888705L;

    public KasperMap() {
        super("map");
        data = new HashMap<String, KasperObject>(5);
    }

    protected KasperMap(String type) {
        super(type);
        data = new HashMap<String, KasperObject>();
    }

    public KasperMap put (String key, KasperObject value){
        toMap().put(key, value);
        ProtectedUtils.setParent(value, this);
        return this;
    }

    public KasperMap put (String key, String value){
        this.put(key, new KasperString(value));
        return this;
    }


    /**
     * Sets the internal data structure to be thread safe.
     */
    public KasperMap setConcurrent(){
        data = new ConcurrentHashMap<>();
        return this;
    }

    public KasperObject get (String key){
        return (KasperObject) ((Map)data).get(key);
    }




    @NotNull
    @Override
    public Iterator<KasperObject> iterator() {
        return toMap().values().iterator();
    }


}

