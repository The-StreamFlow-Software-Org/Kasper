package Server.SuperClass;

import DataStructures.KasperCollection;
import DataStructures.KasperNode;
import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Exceptions.NoSuchKasperObject;
import KasperCommons.Parser.PathParser;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public class KasperGlobalMap implements Serializable {
    @Serial
    private static final long serialVersionUID = 2804015076581134962L;

    public static void instantiate(){
        instance = new KasperGlobalMap();
    }
    public static ConcurrentHashMap<String, KasperNode> globalmap;


    private static KasperGlobalMap instance = null;


    public static ConcurrentHashMap<String, KasperNode> getNodes(){
        return instance.globalmap;
    }

    private KasperGlobalMap () {
        globalmap = new ConcurrentHashMap<>();
    }

    public static KasperNode getNode (String name){
        var x = instance.globalmap.get(name);
        if (x == null) throw new NoSuchKasperObject("node '" + name + "' does not exist");
        return x;
    }

    public static KasperNode newNode(String name){
        var node = new KasperNode(name);
        instance.globalmap.put(name, node);
        return node;
    }

    public static KasperObject findWithPath(String path) {
        PathParser parser = new PathParser();
        var list = parser.unparsePath(path);
        if (list.size() == 1) {
            KasperMap collections = new KasperMap();
            return getNode(list.get(0));
        } else if (list.size() == 2) {
            var currnode = getNode(list.get(0));
            var thisCollection = currnode.useCollection(list.get(1));
            return thisCollection;
        } else if (list.size() == 3) {
            var currnode = getNode(list.get(0));
            return currnode.useCollection(list.get(1)).getValue(list.get(2));
        }
        else {
            var currnode = getNode(list.get(0));
            var object =  currnode.useCollection(list.get(1)).getValue(list.get(2));
            var currobject= object;
            for (int i = 3; i < list.size(); i++) {
                if (currobject instanceof KasperMap m) {
                    currobject = m.get(list.get(i));
                    if (currobject == null) throw new NoSuchKasperObject("Specified object with path: '" + path + "' does not exist.");
                } else if (currobject instanceof KasperList l) {
                    var key = list.get(i);
                    try {
                        if (key.equals("head")) {
                            currobject = l.toList().getFirst();
                        } else if (key.equals("tail")) {
                            currobject = l.toList().getLast();
                        } else {
                            int index = Integer.parseInt(key);
                            var ll = (LinkedList)l.toList();
                            currobject = (KasperObject) ll.get(index);
                        }
                    } catch (Exception e) {
                        if (e instanceof NoSuchElementException) throw new KasperException("Reason:> The list is empty. Unable to use nested queries on this object.");
                        if (e instanceof ArrayIndexOutOfBoundsException) throw new KasperException("Reason:> Your index is invalid, array index out of bounds.");
                        throw new KasperException("Reason:> Invalid list index was found. Use values [head/tail] to add an element to the head or tail respectively. Else, use a numeric string to specify the index.");
                    }
                }
                else {
                    throw new NoSuchKasperObject("Specified object with path: '" + path + "' does not exist.");
                }
            } return currobject;
        }
    }


    public static KasperMap getAllFromCollection (KasperCollection collection) {
        KasperMap map = new KasperMap();
        for (var x : collection.iterate()) {
            map.put(x.getKey(), x.getValue());
        } return map;
    }
}
