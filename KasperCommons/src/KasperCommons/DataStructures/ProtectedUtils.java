package KasperCommons.DataStructures;

import KasperCommons.Parser.PathParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProtectedUtils {

    public static void addReference (KasperObject setInside, KasperObject parent, String as) {
        setInside.addReference(parent, as);
    }

    public static HashMap<KasperObject, String> getReferences (KasperObject object) {
        return object.referencedBy;
    }

    public static Set<Map.Entry<KasperObject, String>> getReferenceIterable (KasperObject object) {
        if (object.referencedBy == null) return null;
        return object.referencedBy.entrySet();
    }

    public static void updateTo (KasperObject old, KasperObject newObj, String path) {
        if (old.parent == null) return;
        var unparsed = PathParser.unparsePath(path);
        var signature = unparsed.get(unparsed.size()-1);
        var parent = old.parent;
        setParentsToNewChild(newObj, parent, signature);
        var iterables = getReferenceIterable(old);
        if (iterables != null) {
            for (var x : iterables) {
                var key = x.getKey();
                var value = x.getValue();
                setParentsToNewChild(newObj, key, value);
            }
        }
        newObj.parent = old.parent;
        // REMEMBER TO UPDATE CACHE!
    }

    private static void setParentsToNewChild(KasperObject newObj, KasperObject key, String value) {
        if (key instanceof KasperMap map) {
            map.put(value, newObj);
        } else if (key instanceof KasperList list) {
            if (value.equals("head")) list.toList().set(0, newObj);
            else if (value.equals("tail")) list.toList().set(list.toList().size()-1, newObj);
            else list.toList().set(Integer.parseInt(value), newObj);
        }
    }

    public static void setParent (KasperObject child, KasperObject parent) {
        child.parent = parent;
    }




}
