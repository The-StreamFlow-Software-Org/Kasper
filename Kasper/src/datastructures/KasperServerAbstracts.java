package datastructures;

import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.datastructures.KasperPathReference;
import com.kasper.commons.exceptions.NoSuchKasperObject;
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
        var data = this.getData().get(key);
        return data;
    }


    public KasperPathReference generaterawPathReference(String path) {
        return new KasperPathReference(path);
    }
}
