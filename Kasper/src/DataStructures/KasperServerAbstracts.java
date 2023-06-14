package DataStructures;

import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Exceptions.NoSuchKasperObject;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public abstract class KasperServerAbstracts extends KasperObject {



    protected ConcurrentHashMap<String, KasperObject> data;
    protected KasperObject parent = null;
    protected Node thisNode;

    protected KasperServerAbstracts(String type) {
        super(type);
        data = new ConcurrentHashMap<>(100);
    }

    protected String name;

    public String getName() {
        return name;
    }

    public ConcurrentHashMap<String, KasperObject> getData() {
        return data;
    }

    protected KasperObject get (String key)  {
        var data = getData().get(key);
        if (data == null) {
            throw new NoSuchKasperObject(key + " does not exist in " + this.getType() + " " + getName());
        } return data;
    }
}
