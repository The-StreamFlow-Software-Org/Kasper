package datastructures;

import com.kasper.commons.annotations.Dangerous;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.datastructures.KasperObject;
import server.SuperClass.KasperGlobalMap;

import java.util.concurrent.ConcurrentHashMap;

public class KasperRelationship extends KasperMap {

    // Specification for this map
    // String id, identified by 'named' keyword
    // String path, which is the path to the object
    protected ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>();


    public KasperRelationship() {
        super("relationship");
        this.parent = parent;
    }

    protected ConcurrentHashMap<String, String> data() {
        return data;
    }


    // breaks a relationship
    // from one object to another
    public void breakRelationship (String id) {
        data().remove(id);
    }


    // Gets an object by ID.
    // If the object is already deleted from the GlobalMap
    // it means that the object is marked dead, and is deleted from this
    // relationship.
    public KasperObject getByID (String id) {
        var finds = data().get(id);
        if (finds == null) return null;
        KasperObject found = null;
        try {
            found = KasperGlobalMap.findWithPath(finds);
        } catch (Exception e) {
            data.remove(id);
            return null;
        } return found;
    }


}
