package DataStructures;

import KasperCommons.DataStructures.KasperObject;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public abstract class KasperServerAbstracts extends KasperObject {

    protected ConcurrentHashMap<String, KasperObject> data;
    protected KasperObject parent = null;
    protected Node thisNode;

    protected KasperServerAbstracts(String type) {
        super(type);
        data = new ConcurrentHashMap<>();
    }

    protected String name;

    public String getName() {
        return name;
    }

    public ConcurrentHashMap<String, KasperObject> getData() {
        return data;
    }
}
