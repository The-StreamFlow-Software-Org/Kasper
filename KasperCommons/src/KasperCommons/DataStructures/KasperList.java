package KasperCommons.DataStructures;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * This KasperList class is a data class that
 * holds internal data for a list-type KasperObject.
 * Internally, it uses a LinkedList for its implementation.
 */
public class KasperList extends KasperObject{


    public KasperList() {
        super("list");
        data = new LinkedList<>();
    }

    /**
     *
     * @param object pushes this KasperObject to the list.
     */
    public KasperList addToList(KasperObject object) {
        toList().add(object);
        return this;
    }

    /**
     * Sets the internal data structure to be thread safe.
     */
    public KasperList setConcurrent(){
        data = new ConcurrentLinkedDeque<>();
        return this;
    }

    /**
     *
     * @param args is an overloaded variadic args to accept variadic amount of KasperObjects
     */
    public KasperList addToList (KasperObject ... args) {
        for (var x : args) {
            toList().add(x);
        } return this;
    }


    /**
     *
     * @param args is an overloaded variadic args to accept variadic amount of strings
     */
    public KasperList addToList (String ... args) {
        for (var x : args) {
            toList().add(new KasperString(x));
        } return this;
    }






}
