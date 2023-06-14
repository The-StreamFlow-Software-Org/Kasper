package Server.SuperClass;

import DataStructures.KasperCollection;
import DataStructures.KasperNode;
import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Exceptions.NoSuchKasperObject;
import KasperCommons.Parser.PathParser;
import Persistence.Serialize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class KasperGlobalMap implements Serializable {
    public static ConcurrentHashMap<String, KasperNode> globalmap;


    private static KasperGlobalMap instance = null;


    public static ConcurrentHashMap<String, KasperNode> getNodes(){
        if (instance == null) instance = new KasperGlobalMap();
        return instance.globalmap;
    }

    private KasperGlobalMap () {
        globalmap = new ConcurrentHashMap<>();
    }

    public static KasperNode getNode (String name){
        if (instance == null) instance = new KasperGlobalMap();
        var x = instance.globalmap.get(name);
        if (x == null) throw new NoSuchKasperObject("node '" + name + "' does not exist");
        return x;
    }

    public static KasperNode newNode(String name){
        if (instance == null) instance = new KasperGlobalMap();
        var node = new KasperNode(name);
        instance.globalmap.put(name, node);
        return node;
    }

    public static KasperObject findWithPath(String path) {
        var list = PathParser.unparsePath(path);
        if (list.size() == 1) {
            KasperMap collections = new KasperMap();
            var currnode = getNode(list.get(0));
            for (var x : currnode.iterate()) {
                collections.put(x.getKey(),getAllFromCollection((KasperCollection) x.getValue()));
            } return collections;
        } else if (list.size() == 2) {
            var currnode = getNode(list.get(0));
            var thisCollection = currnode.useCollection(list.get(1));
            return getAllFromCollection(thisCollection);
        } else if (list.size() == 3) {
            var currnode = getNode(list.get(0));
            return currnode.useCollection(list.get(1)).getValue(list.get(2));
        }
        else {
            KasperMap collections = new KasperMap();
            var currnode = getNode(list.get(0));
            var object =  currnode.useCollection(list.get(1)).getValue(list.get(2));
            var currobject= object;
            for (int i = 3; i < list.size(); i++) {
                if (currobject instanceof KasperMap m) {
                    currobject = m.get(list.get(i));
                    if (currobject == null) throw new NoSuchKasperObject("Specified object with path: '" + path + "' does not exist.");
                } else {
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
