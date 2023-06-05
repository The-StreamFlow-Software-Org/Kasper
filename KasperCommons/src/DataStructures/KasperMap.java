package DataStructures;

import java.util.HashMap;
import java.util.Map;

public class KasperMap extends KasperObject{

    public KasperMap() {
        super("map");
        data = new HashMap<KasperString, KasperObject>();
    }

    public void put (String key, KasperObject value){
        toMap().put(new KasperString(key), value);
    }

    public void put (KasperString key, KasperObject value){
        toMap().put(key, value);
    }



}
