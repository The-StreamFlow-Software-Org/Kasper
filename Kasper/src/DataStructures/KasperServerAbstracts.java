package DataStructures;

import KasperCommons.DataStructures.KasperMap;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.DataStructures.KasperPathReference;
import KasperCommons.Exceptions.NoSuchKasperObject;
import org.w3c.dom.Node;

import java.io.Serial;
import java.util.concurrent.ConcurrentHashMap;

public abstract class KasperServerAbstracts extends KasperMap {


    @Serial
    private static final long serialVersionUID = 2405370640391496865L;
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

    public KasperObject get (String key)  {
        var data = getData().get(key);
        if (data == null) {
            throw new NoSuchKasperObject("\nReason:> '" + key + "' does not exist in " + this.getType() + " '" + getName() + "'");
        } return data;
    }


    public KasperPathReference generaterawPathReference(String path) {
        return new KasperPathReference(path);
    }
}
