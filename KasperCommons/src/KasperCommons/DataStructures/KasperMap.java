package KasperCommons.DataStructures;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The KasperMap is a class representing the map data structure.
 * It can represent objects.
 */
public class KasperMap extends KasperObject implements Iterable<KasperObject>{

    public KasperMap() {
        super("map");
        data = new HashMap<KasperString, KasperObject>();
    }

    public KasperMap put (String key, KasperObject value){
        toMap().put(new KasperString(key), value);
        return this;
    }

    public KasperMap put (String key, String value){
        toMap().put(new KasperString(key), new KasperString(value));
        return this;
    }

    public KasperMap put (KasperString key, KasperObject value){
        toMap().put(key, value);
        return this;
    }

    /**
     * Sets the internal data structure to be thread safe.
     */
    public KasperMap setConcurrent(){
        data = new ConcurrentHashMap<>();
        return this;
    }

    @Override
    public Map automaticallyCast() {
        return toMap();
    }

    @NotNull
    @Override
    public Iterator<KasperObject> iterator() {
        return toMap().values().iterator();
    }
}
