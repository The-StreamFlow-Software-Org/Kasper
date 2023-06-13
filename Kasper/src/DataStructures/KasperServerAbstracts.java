package DataStructures;

import KasperCommons.DataStructures.KasperObject;
import org.w3c.dom.Node;

import java.util.HashMap;

public abstract class KasperServerAbstracts extends KasperObject {

    protected HashMap<String, KasperObject> data;
    protected KasperObject parent = null;
    protected Node thisNode;

    protected KasperServerAbstracts(String type) {
        super(type);
        data = new HashMap<>();
    }

    protected String name;

    public String getName() {
        return name;
    }

    public HashMap<String, KasperObject> getData() {
        return data;
    }
}
