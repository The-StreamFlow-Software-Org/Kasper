package stateholder.functions;

import com.kasper.commons.datastructures.*;
import com.kasper.commons.exceptions.KasperException;
import com.kasper.commons.exceptions.KasperObjectAlreadyExists;
import com.kasper.commons.exceptions.NonCollectionTypeException;
import com.kasper.commons.exceptions.NotIterableException;
import computations.NioDeleteResolver;
import datastructures.KasperCollection;
import datastructures.KasperNode;
import datastructures.KasperRelationship;
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
    public static int ENTITY = 3;


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
     *
     * - delete
     *     path
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
                    ProtectedUtils.setParent(object, map);
                    map.put(key, object);
                } else {
                    if (key.equals("head")) {
                        ((KasperList) node).addFirst(object);
                        ProtectedUtils.setParent(object, node);
                    } else if (key.equals("tail")) {
                        ((KasperList) node).addLast(object);
                        ProtectedUtils.setParent(object, node);
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

            functions.put("delete", (args)-> {
                String path = (String) args.get("path");
                if (args.get("type").equals(ENTITY)) {
                    var assertion = KasperGlobalMap.findWithPath(path);
                    if (assertion instanceof KasperCollection || assertion instanceof KasperNode) {
                        throw new KasperException("Cannot delete collection or node using query command 'DELETE ENTITY', use respective 'DELETE NODE' or 'DELETE COLLECTION' instead.");
                    } else NioDeleteResolver.deleteObject(path);
                }
                return null;
            });
        }
    }
}
