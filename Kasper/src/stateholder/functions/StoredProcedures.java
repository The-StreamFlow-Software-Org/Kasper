package stateholder.functions;

import com.kasper.commons.datastructures.KasperList;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.exceptions.KasperException;
import com.kasper.commons.exceptions.NonCollectionTypeException;
import com.kasper.commons.exceptions.NotIterableException;
import datastructures.KasperRelationship;
import parser.executor.ExecutionQueue;
import parser.executor.ExecutionUnit;
import parser.tokens.FunctionToken;
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
            functions.put("insert", (args)->{
                var object = (KasperObject) args.get("object");
                var path = (String) args.get("path");
                var key = (String) args.get("key");
                var node = KasperGlobalMap.findWithPath(path);
                if (!(node instanceof KasperMap || node instanceof KasperList)) {
                    throw new NonCollectionTypeException(path);
                }
                if (node instanceof KasperMap) {
                    ((KasperMap) node).put(key, object);
                } else {
                    if (key.equals("head")) {
                        ((KasperList) node).toArray().addFirst(object);
                    } else if (key.equals("tail")) {
                        ((KasperList) node).addToList(object);
                    } else {
                        throw new IndexNotViableException(path, key);
                    }
                } return null;
            });
            functions.put("create", (args) ->{
                int type = (Integer) args.get("type");
                String name = (String) args.get("name");
                if (type==COLLECTION) {
                    String parent = (String) args.get("parent");
                    KasperGlobalMap.findWithPath(parent).castToMap().put(name, new KasperMap());
                } else if (type==NODE) {
                    KasperGlobalMap.newNode(name);
                } else if (type==RELATIONSHIP) {
                    String path = (String) args.get("parent");
                    KasperObject obj = KasperGlobalMap.findWithPath(path);
                    if (!(obj instanceof KasperMap)) throw new KasperException("Cannot create relationship in non-map object");
                    ((KasperMap) obj).put(name, new KasperRelationship());
                } return null;
            });
            functions.put("get", (args) -> {
                String path = (String) args.get("path");
                return KasperGlobalMap.findWithPath(path);

                // RESOLVE INCLUDE LATER
            });
        }
    }
}
