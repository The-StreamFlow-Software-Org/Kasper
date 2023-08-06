package com.kasper.commons.datastructures;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This KasperList class is a data class that
 * holds internal data for a list-type KasperObject.
 * Internally, it uses a LinkedList for its implementation.
 */
public class KasperList extends KasperObject implements Iterable{


    @Serial
    private static final long serialVersionUID = 3831607456994963123L;

    public KasperList() {
        super("list");
        data = new LockedLL<>();
    }

    /**
     *
     * @param object pushes this KasperObject to the list.
     */
    public KasperList addToList(KasperObject object) {
        object.parent = this;
        toList().add(object);
        return this;
    }



    /**
     *
     * @param args is an overloaded variadic args to accept variadic amount of KasperObjects
     */
    public KasperList addToList (KasperObject ... args) {
        for (var x : args) {
            addToList(x);
        } return this;
    }


    /**
     *
     * @param args is an overloaded variadic args to accept variadic amount of strings
     */
    public KasperList addToList (String ... args) {
        for (var x : args) {
            addToList(new KasperString(x));
        } return this;
    }

    /**
     *
     * @return an ArrayList containing all the data in the list.
     */
    public LinkedList<KasperObject> toArray (){
        var x = toList();
        return x.getInternalArray();
    }

    public KasperList addFirst(KasperObject object){
        toList().addFirst(object);
        object.parent = this;
        return this;
    }

    public KasperList addLast (KasperObject object){
        toList().addLast(object);
        object.parent = this;
        return this;
    }


    @NotNull
    @Override
    public Iterator<KasperObject> iterator() {
        return toList().iterator();
    }
}
