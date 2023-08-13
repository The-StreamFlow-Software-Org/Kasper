package com.kasper.commons.datastructures;

import com.kasper.commons.exceptions.KasperException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProtectedUtils {

    // add reference adds a marker that 'parent' has a reference to 'setInside'.
    // the 'as' parameter can differ. in lists, this is the index as a string.
    // in maps, this is the key. this is for easy access from the parent.
    // in KasperRelationships, the 'as' key is also held as to easily remove it.
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

    // Here, you pass in the unparsed path
    public static void updateTo (KasperObject old, KasperObject newObj) {
        if (old.parent == null) return;
        var signature = getID(old);
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
            if (value.equals("head")) list.toList().add(newObj);
            else if (value.equals("tail")) list.toList().addLast(newObj);
            else throw new KasperException("Invalid position specified. Only 'head' and 'tail' access is allowed.");
        }
    }

    public static void setParent (KasperObject child, KasperObject parent) {
        child.parent = parent;
    }

    public static String getID (KasperObject o) {
        if (o.id != null && !o.id.isEmpty()) return o.id;
        if (o.parent instanceof KasperMap) {
            for (var x : o.parent().toMap().entrySet()) {
                if (x.getValue() == o) {
                    o.id = x.getKey();
                    break;
                }
            }
        } else if (o.parent instanceof KasperList) {
            int index = 0;
            for (var x : o.parent().toList()) {
                if (x == o) {
                    o.id = Integer.toString(index);
                    break;
                } index++;
            }
        } else {
            throw new KasperException("Please contact the KasperTeam. There seems to be an error in the method 'getID' in ProtectedUtils.");
        }
        return o.id;
    }

    // set any data of any KasperObject
    // unsafe
    public static void setData (KasperObject o, Object nullable) {
        o.data = nullable;
    }



    public static void setInteralObjectToNull (KasperObject o) {

    }

    // gets the data of the object
    // unsafe
    public static Object getData (KasperObject o) {
        return o.data;
    }




}
