package com.kasper.commons.datastructures;


import com.kasper.commons.exceptions.NotIterableException;

import java.io.Serializable;
import java.util.*;

/**
 * The KasperObject class is a general class used to hold
 * complex KasperObject data structures, using polymorphic
 * design structures.
 */
public class KasperObject implements Serializable {

    // for disabling and enabling EXPERIMENTAL_MODE
    protected static boolean EXPERIMENTAL_MODE = false;
    protected KasperObject parent;

    // listening threads asking for path
    protected List<Thread> inquirers;

    protected void setParent (KasperObject parent) {
        this.parent = parent;
    }


    // holds the final path upon path construction
    protected String finalPath = "";

    protected HashMap<KasperObject, String> referencedBy = null;

    protected void addReference(KasperObject parent, String as) {
        if (referencedBy == null) referencedBy = new HashMap<>();
        referencedBy.put(parent, as);
    }

    public String getPath() {
        if (finalPath.equals("")) {
            if (inquirers == null) inquirers = new LinkedList<>();
            if (id.equals("")) {
                inquirers.add(Thread.currentThread());
                try {
                    while (true) Thread.sleep(1000000000);
                } catch (InterruptedException e) {}
            } resolvePath(this);
        } return finalPath;
    }

    public KasperObject parent() {
        return parent;
    }

    protected void resolvePath(KasperObject current) {
        if (!current.finalPath.equals("")) return;
        if (!current.parent.finalPath.equals("")) {
            current.finalPath = current.parent.finalPath + "." + id;
            return;
        }
        resolvePath(current.parent);
    }

    private static final long serialVersionUID = -7689140920033408553L;

    /*
    Contains the path of the topmost object in the hierarchy. Returns NULL
     */
    protected String id = "";

    public KasperObject setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Type is used to store and introspect the type
     * of this data structure. This can be of type
     * 'string', 'list', or 'map'.
     */
    private String type;

    /**
    Holds the internal data structure. Can be a Map<KasperString, KasperObject>,
    an List<KasperObject>, or a simple String.
     */
    protected Object data;

    /**
     *
     * @return a string representation of its type. Can be 'map', 'string', or 'list'.
     * @note: relationships are considered as maps, while the references inherit their referenced type.
     */
    public String getType (){
        return type;
    }

    /*
    Should only be accessed with extended classes.
     */
    protected KasperObject (String type) {
        this.type = type.intern();
    }


    @SuppressWarnings("unchecked")
    /**
     *
     * @return the data of this object as a list.
     * @throws ClassCastException if the original type of this object is not a list.
     */
    public LockedLL<KasperObject> toList (){
        return (LockedLL<KasperObject>) data;
    }

    /**
     *
     * @return the data of this object as a string.
     * @throws ClassCastException if the original type of this object is not a string.
     */
    public String toStr (){
        return (String)data;
    }

    @SuppressWarnings("unchecked")
    /**
     *
     * @return the data of this object as a map.
     * @throws ClassCastException if the original type of this object is not a map.
     */
    public Map<String, KasperObject> toMap (){
        return (Map) data;
    }

    public static KasperString str (String str) {
        return new KasperString(str);
    }

    public KasperList castToList(){
        return (KasperList) this;
    }

    public KasperString castToString(){
        return (KasperString) this;
    }

    public KasperMap castToMap(){
        return (KasperMap) this;
    }

    @Override
    public String toString() {
        if (this instanceof KasperMap m) {
            return m.toMap().toString();
        } else if (this instanceof KasperList l){
            return l.toList().toString();
        } else if (this instanceof KasperString s){
            return s.toString();
        }
        return super.toString();
    }

    public Iterable getIterable(){
        if (this instanceof KasperList list) return list.toList();
        if (this instanceof KasperMap map) return map.toMap().entrySet();
        throw new NotIterableException(id);
    }
}
