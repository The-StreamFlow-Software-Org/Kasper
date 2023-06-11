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
        data = new HashMap<String, KasperObject>();
    }

    public KasperMap put (String key, KasperObject value){
        toMap().put(key, value);
        return this;
    }

    public KasperMap put (String key, String value){
        toMap().put(key, new KasperString(value));
        return this;
    }


    /**
     * Sets the internal data structure to be thread safe.
     */
    public KasperMap setConcurrent(){
        data = new ConcurrentHashMap<>();
        return this;
    }




    @NotNull
    @Override
    public Iterator<KasperObject> iterator() {
        return toMap().values().iterator();
    }


}

