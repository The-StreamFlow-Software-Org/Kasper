package KasperCommons.DataStructures;


import KasperCommons.Exceptions.UniteratableObjectException;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Map;

/**
 * The KasperObject class is a general class used to hold
 * complex KasperObject data structures, using polymorphic
 * design structures.
 */
public class KasperObject implements Serializable {


    private static final long serialVersionUID = -7689140920033408553L;

    /*
    Contains the path of the topmost object in the hierarchy. Returns NULL
     */
    public String path = "";

    public KasperObject setPath(String path) {
        this.path = path;
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
     */
    public String getType (){
        return type;
    }

    /*
    Should only be accessed with extended classes.
     */
    protected KasperObject (String type) {
        this.type = type;
    }


    @SuppressWarnings("unchecked")
    /**
     *
     * @return the data of this object as a list.
     * @throws ClassCastException if the original type of this object is not a list.
     */
    public LinkedList<KasperObject> toList (){
        return (LinkedList<KasperObject>) data;
    }

    /**
     *
     * @return the data of this object as a string.
     * @throws ClassCastException if the original type of this object is not a string.
     */
    public String toStr (){
        return (String) data;
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
        throw new UniteratableObjectException(path);
    }
}
