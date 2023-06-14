package Server.SuperClass;

import DataStructures.KasperNode;

import java.util.concurrent.ConcurrentHashMap;

public class KasperGlobalMap {
    private ConcurrentHashMap<String, KasperNode> globalmap;
    private static KasperGlobalMap instance = null;

    public static ConcurrentHashMap<String, KasperNode> getNodes(){
        if (instance == null) instance = new KasperGlobalMap();
        return instance.globalmap;
    }

    private KasperGlobalMap () {
        globalmap = new ConcurrentHashMap<>();
    }

    public static KasperNode getNode (String name){
        return instance.globalmap.get(name);
    }
}
