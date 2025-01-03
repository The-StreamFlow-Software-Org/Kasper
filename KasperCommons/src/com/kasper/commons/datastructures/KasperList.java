package com.kasper.commons.datastructures;



import java.io.Serial;
import java.util.ArrayList;
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
        addLast(object);
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
        getSafeData().addFirst(object);
        object.parent = this;
        return this;
    }

    protected LinkedList<KasperObject> data() {
        return ((LockedLL) this.data).internalArray;
    }

    public KasperList addLast (KasperObject object){
        getSafeData().addLast(object);
        object.parent = this;
        return this;
    }

    public KasperObject popFirst() {
        return getSafeData().removeFirst();
    }

    public KasperObject popLast(){
        return getSafeData().removeLast();
    }

    public int size() {
        return getSafeData().size();
    }

    protected LockedLL<KasperObject> getSafeData()  {
        return (LockedLL<KasperObject>) data;
    }


    @Override
    public Iterator<KasperObject> iterator() {
        return toList().iterator();
    }
}
