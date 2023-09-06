package stateholder.functions;

import com.kasper.commons.Parser.PathParser;
import com.kasper.commons.datastructures.*;
import com.kasper.commons.exceptions.*;
import computations.NioDeleteResolver;
import datastructures.KasperCollection;
import datastructures.KasperNode;
import datastructures.KasperRelationship;
import parser.exceptions.Throw;
import parser.executor.ExecutionUnit;
import parser.tokens.Operator;
import server.Handler.IndexNotViableException;
import server.SuperClass.KasperGlobalMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     *
     *  - assert
     *    path: String
     *    operator: OperatorToken
     *    value: Object
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
                if (args.get("alias") != null) {
                    KasperMap map = new KasperMap();
                    ProtectedUtils.setData(map, KasperGlobalMap.globalmap);
                    return map;
                }
                String path = (String) args.get("path");
               return KasperGlobalMap.findWithPath(path);
                // RESOLVE INCLUDE LATER
            });

            functions.put("delete", (args)-> {
                String path = (String) args.get("path");
                String name = (String) args.get("name");

                if (args.get("type").equals(ENTITY)) {
                    var assertion = KasperGlobalMap.findWithPath(path);
                    if (assertion instanceof KasperCollection || assertion instanceof KasperNode) {
                        throw new KasperException("Cannot delete collection-type or node-type structures using query command 'DELETE ENTITY', use respective 'DELETE NODE' or 'DELETE COLLECTION' instead.");
                    } else NioDeleteResolver.deleteObject(path);
                } else if (args.get("type").equals(COLLECTION)) {
                    PathParser parser = new PathParser();
                    parser.addPath(name);
                    String fullPath = path + "." + parser.parsePath();
                    var assertion = KasperGlobalMap.findWithPath(fullPath);
                    if (assertion == null) throw new KasperException("Cannot delete non-existent collection '" + name + "' in '" + path + "'.");
                    if (!(assertion instanceof KasperCollection)) {
                        throw new KasperException("Cannot delete non-collection structures found in '" + fullPath + "' using query command 'DELETE COLLECTION', use respective 'DELETE NODE' or 'DELETE ENTITY' instead.");
                    } else {
                        NioDeleteResolver.deleteObject(fullPath);
                    }
                } else if (args.get("type").equals(NODE)) {
                    var assertion = KasperGlobalMap.findWithPath(path);
                    if (!(assertion instanceof KasperNode)) {
                        throw new KasperException("Cannot delete non-node structures using query command 'DELETE NODE', use respective 'DELETE COLLECTION' or 'DELETE ENTITY' instead.");
                    } else NioDeleteResolver.deleteObject(path);
                } else if (args.get("type").equals(RELATIONSHIP)) {
                    PathParser parser = new PathParser();
                    parser.addPath(name);
                    String fullPath = path + "." + parser.parsePath();
                    var assertion = KasperGlobalMap.findWithPath(fullPath);
                    if (assertion == null) throw new KasperException("Cannot delete non-existent relationship '" + name + "' in '" + path + "'.");
                    if (!(assertion instanceof KasperRelationship)) {
                        throw new KasperException("Cannot delete non-relationship structures found in '" + fullPath + "' using query command 'DELETE RELATIONSHIP', use respective 'DELETE COLLECTION' or 'DELETE ENTITY' instead.");
                    } else {
                        NioDeleteResolver.deleteObject(fullPath);
                    }
                } else {
                    throw new KasperException("Invalid entity-type for delete command");
                }
                System.gc();
                return null;
            });


            functions.put("assert", (args)->{
                String path = (String) args.get("path");
                Operator operator = (Operator) args.get("operator");
                Object value = args.get("value");
                KasperObject internalComparator = KasperGlobalMap.findWithPath(path);
                var assertion = KasperGlobalMap.findWithPath(path);
                Throw.assertOperatorValidity(operator.getName(), internalComparator);
                Throw.typeChecking(internalComparator, value);
                if (operator.OperatorType == Operator.EQUAL) {
                    if (internalComparator.equals(value)) return new KasperInteger(1);
                    else return new KasperInteger(0);
                } else if (operator.OperatorType == Operator.NOT_EQUAL) {
                    if (internalComparator.equals(value)) return new KasperInteger(0);
                    else return new KasperInteger(1);
                } else {
                    throw new KasperException("Operator " + operator.getName() + " is not supported in assert statement");
                }
            });

            // MATCH IMPLEMENTATION

            /*
            MATCH ARGUMENTS:
            match-type: GET, DELETE, ASSERT (STRING)
            operator: operator-type (INT)
            having: attributes (STRING)
            value: value (STRING)
             */
            functions.put("match", (args)->{
                String matchType = (String) args.get("match-type");
                int operator = (int) args.get("operator");
                String having = (String) args.get("having");
                Object value = args.get("value");
                var matches = getAllThatMatches((String)args.get("path"), having);
                if (matchType.equals("GET")) {
                    return getAllThatMatchOperator(getAllThatMatches((String)args.get("path"), having), having, operator, (String) value);
                } else {
                    throw new SyntaxError("Unsupported MATCH operation.");
                }
            });

        }
    }

    public static LinkedList<KasperMap> getAllThatMatches(String path, String having) {
        var assertion = KasperGlobalMap.findWithPath((String) path);
        if (assertion == null) throw new KasperException("Cannot find object '" + path + "'.");
        if (assertion instanceof KasperPrimitive) {
            throw new KasperException("Cannot use MATCH operation on primitive type (INTEGER/DECIMAL/STRING).");
        }
        LinkedList<KasperMap> matches = new LinkedList<>();
        if (assertion instanceof KasperMap){
            for (var entry : assertion.castToMap()) {
                if (entry instanceof KasperMap entryMap) {
                    if (entryMap.get((having)) != null) matches.push(entryMap);
                }
            }
        } else {
            for (var obj : assertion.toList()) {
                if (obj instanceof KasperMap entryMap) {
                    if (entryMap.get((having)) != null) matches.push(entryMap);
                }
            }
        } return matches;
    }

    public static KasperList getAllThatMatchOperator (LinkedList<KasperMap> partial, String having, int operatorType, String argument) {
        KasperList matches = new KasperList();
        if (operatorType == Operator.LIKE) {
            Pattern pattern =  Pattern.compile(argument);
            for (var entry : partial) {
                var result = entry.get(having);
                if (result instanceof KasperString) {
                    Matcher matcher = pattern.matcher(result.toStr());
                    if (matcher.matches()) matches.addLast(entry);
                } else throw new KasperException("Cannot use LIKE operator on non-string type.");
            } return matches;
        } else {
            throw new KasperException("Unsupported operator type for MATCH.");
        }

    }
}
