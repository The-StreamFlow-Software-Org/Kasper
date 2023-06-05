package DataStructures;

import java.util.Deque;
import java.util.Map;

/**
 * The KasperObject class is a general class used to hold
 * complex KasperObject data structures, using polymorphic
 * design structures.
 */
public class KasperObject {

    /**
     Type is used to store and introspect the type
     of this data structure. This can be of type
     'string', 'list', or 'map'.
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


    /**
     *
     * @return the data of this object as a list.
     * @throws ClassCastException if the original type of this object is not a list.
     */
    public Deque<KasperObject> toList (){
        return (Deque) data;
    }

    /**
     *
     * @return the data of this object as a string.
     * @throws ClassCastException if the original type of this object is not a string.
     */
    public String toString (){
        return (String) data;
    }

    /**
     *
     * @return the data of this object as a map.
     * @throws ClassCastException if the original type of this object is not a map.
     */
    public Map toMap (){
        return (Map) data;
    }



}
