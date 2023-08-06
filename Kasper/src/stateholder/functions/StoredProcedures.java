package stateholder.functions;

import com.kasper.commons.datastructures.KasperList;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.datastructures.LocalPathCrawler;
import com.kasper.commons.exceptions.KasperException;
import com.kasper.commons.exceptions.KasperObjectAlreadyExists;
import com.kasper.commons.exceptions.NonCollectionTypeException;
import datastructures.KasperCollection;
import datastructures.KasperNode;
import com.kasper.commons.datastructures.KasperRelationship;
import parser.executor.ExecutionUnit;
import server.Handler.IndexNotViableException;
import server.SuperClass.KasperGlobalMap;

import java.util.HashMap;

public class StoredProcedures {

    private static HashMap<String, ExecutionUnit> functions = null;

    public static KasperObject execute(String functionName, HashMap<String, Object> args) throws KasperException{
        verifyInitialization();
        return functions.get(functionName).databaseTask(args);
    }

    public static int NODE = 0;
    public static int COLLECTION = 1;
    public static int RELATIONSHIP = 2;


    /**
     * Args for the following:
     * - insert
     *     object: KasperObject
     *     path: String
     *     key : String
     *
     * - create
     *     type: int either node/collection/relationship
     *     name: String
     *     parent: String (for collection only)
     *
     * - get
     *     path: String
     */
    private static void verifyInitialization() {
        if (functions == null) {
            functions = new HashMap<>();


            // INSERT IMPLEMENTATION
            functions.put("insert", (args)->{
                var object = (KasperObject) args.get("object");
                var path = (String) args.get("path");
                var key = (String) args.get("key");
                var node = KasperGlobalMap.findWithPath(path);
                if (!(node instanceof KasperMap || node instanceof KasperList)) {
                    throw new NonCollectionTypeException(path);
                }
                if (node instanceof KasperMap map) {
                    if (((KasperMap) node).get(key) != null) throw new KasperObjectAlreadyExists("object", key, path);
                    map.put(key, object);
                } else {
                    if (key.equals("head")) {
                        ((KasperList) node).addFirst(object);
                    } else if (key.equals("tail")) {
                        ((KasperList) node).addLast(object);
                    } else {
                        throw new IndexNotViableException(path, key);
                    }
                } return null;
            });


            // CREATE IMPLEMENTATION
            functions.put("create", (args) ->{
                int type = (Integer) args.get("type");
                String name = (String) args.get("name");
                if (type==COLLECTION) {
                    String parent = (String) args.get("parent");
                    var parentNode = KasperGlobalMap.findWithPath(parent);
                    if (parentNode.toMap().get(name) != null) throw new KasperObjectAlreadyExists("collection", name, parent);
                    parentNode.castToMap().put(name, new KasperCollection((KasperNode) parentNode, name));
                } else if (type==NODE) {
                    if (KasperGlobalMap.globalmap.get(name) != null) throw new KasperObjectAlreadyExists("node", name, "the global map");
                    var node = KasperGlobalMap.newNode(name);
                    LocalPathCrawler.finalPathSetter(node, name);
                } else if (type==RELATIONSHIP) {
                    String path = (String) args.get("parent");
                    KasperObject obj = KasperGlobalMap.findWithPath(path);
                    if (obj.toMap().get(name) != null) throw new KasperObjectAlreadyExists("relationship", name, path);
                    if (((!(obj instanceof KasperMap)) || obj instanceof KasperNode || obj instanceof KasperCollection)) throw new KasperException("Cannot create relationship in non-map object");
                    ((KasperMap) obj).put(name, new KasperRelationship());
                } return null;
            });


            // GET IMPLEMENTATION
            functions.put("get", (args) -> {
                String path = (String) args.get("path");
               return KasperGlobalMap.findWithPath(path);
                // RESOLVE INCLUDE LATER
            });
        }
    }
}
