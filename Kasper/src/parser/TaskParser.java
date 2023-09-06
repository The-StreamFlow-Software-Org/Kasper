package parser;

import com.kasper.commons.datastructures.KasperObject;
import parser.exceptions.Throw;
import parser.executor.ExecutionQueue;
import parser.tokens.*;
import stateholder.functions.PreparedStatements;
import stateholder.functions.StoredProcedures;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskParser {
    private ExecutionQueue processes;

    public ExecutionQueue getExecutionQueue() {
        return processes;
    }


    public TaskParser () {
        processes = new ExecutionQueue();
    }

    public boolean checkCache(TokenCursor query){
        // check for tokens
        var cacheResult = PreparedStatements.executeCachedInstance(query);
        if (cacheResult != Boolean.FALSE) {
            processes.resultSet.addResult((KasperObject)cacheResult);
            return true;
        } return false;
    }

    // for assertion, we need to do the following:
    // assert <path> <operator> <value>
    // where <value> is an Object-type, so it must
    // be enclosed in the parenthesis operator
    // (value)
    public Boolean assertFn (TokenCursor tokens) {
        if (checkCache(tokens)) return true;
        var initial = tokens.nextToken();
        StringBuilder builder = new StringBuilder();
        builder.append("ASSERT ").append(initial.getName()).append(" ");
        // Asserting that PATH exists
        Throw.syntaxAssert(builder.toString(), TokenType.STRING, initial.tokenType);
        var path = initial.getName();
        initial = tokens.nextToken();
        builder.append(initial.getName()).append(" ");
        /// Asserting that OPERATOR exists
        Throw.syntaxAssert(builder.toString(), TokenType.OPERATOR, initial.tokenType);
        var operator = initial.toOperator();
        initial = tokens.nextToken();
        builder.append(initial.getName()).append(" ");
        // Asserting that VALUE exists
        Throw.syntaxAssert(builder.toString(), TokenType.OBJECT, initial.tokenType);
        var value = initial.toObject().getInternalObject();
        HashMap<String, Object> args = new HashMap<>();
        args.put("path", path);
        args.put("operator", operator);
        args.put("value", value);
        processes.addProcess("assert", args);
        return null;
    }

    public Boolean create (TokenCursor tokens) {
        if (checkCache(tokens)) return true;
        var entity = tokens.nextToken();
        var initialEntity = entity;
        var initialType = entity.toStatement().type;
        switch (initialType) {
            case RELATIONSHIP:
            case COLLECTION: {
                StringBuilder parsedSoFar = new StringBuilder();
                String entityName;
                String parentName;
                entity = tokens.nextToken();
                entityName = entity.getName();
                // verify if the entity name exists
                parsedSoFar.append("CREATE ").append(initialEntity.getName()).append(" ").append(entity.getName()).append(" ");
                Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STRING, entity.tokenType);
                // verify the IN keyword
                entity = tokens.nextToken();
                parsedSoFar.append(entity.getName()).append(" ");
                Throw.statementAssert(parsedSoFar.toString(), StatementType.IN, entity);
                entity = tokens.nextToken();
                parentName = entity.getName();
                // get the path name here
                parsedSoFar.append(entity.getName());
                Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STRING, entity.tokenType);
                HashMap<String, Object> args = new HashMap<>();
                if (initialType == StatementType.RELATIONSHIP) {
                    args.put("type", StoredProcedures.RELATIONSHIP);
                } else {
                    args.put("type", StoredProcedures.COLLECTION);
                }
                args.put("parent", parentName);
                args.put("name", entityName);
                processes.addProcess("create", args);
                return true;
            }
            case NODE: {
                String nodeName = null;
                entity = tokens.nextToken();
                // verify that collection name exists
                Throw.syntaxAssert("CREATE NODE " + entity.name, TokenType.STRING, entity.tokenType);
                nodeName = entity.name;
                HashMap<String, Object> args = new HashMap<>();
                args.put("type", StoredProcedures.NODE);
                args.put("name", nodeName);
                processes.addProcess("create", args);
                return true;
            }
            default: {
                throw Throw.invalidStatement("CREATE", initialType);
            }
        }
    }

    public Boolean insert (TokenCursor tokens) {
        if (checkCache(tokens)) return true;
        var objectToken = tokens.nextToken();
        StringBuilder parsedSoFar = new StringBuilder();
        parsedSoFar.append("INSERT ").append(objectToken.getName()).append(" ");
        Throw.syntaxAssert(parsedSoFar.toString(), TokenType.OBJECT, objectToken.tokenType);
        // asserting IN
        parsedSoFar.append(tokens.peekNext().toString()).append(" ");
        Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STATEMENT, tokens.peekNext());
        Throw.statementAssert(parsedSoFar.toString(), StatementType.IN, tokens.nextToken());
        // asserting path
        var pathToken = tokens.nextToken();
        parsedSoFar.append(pathToken.getName()).append(" ");
        Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STRING, pathToken.tokenType);
        // asserting AS
        parsedSoFar.append(tokens.weakNextToken().getName());
        Throw.statementAssert(parsedSoFar.toString(), StatementType.AS, tokens.nextToken());
        var keyToken = tokens.nextToken();
        parsedSoFar.append(keyToken.getName());
        Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STRING, keyToken.tokenType);

        // Passing to the 'Insert' stored procedure.
        var newObject = objectToken.toObject().getInternalObject();
        var path = pathToken.toPath().getName();
        var key = keyToken.getName();
        HashMap<String, Object> args = new HashMap<>();
        args.put("object", newObject);
        args.put("path", path);
        args.put("key", key);
        processes.addProcess("insert", args);
        return true;
    }

    public Boolean get (TokenCursor tokens) {
        if (checkCache(tokens)) return true;
        processes.addProcess("get", parseGET(tokens));
        return true;
    }

    public Boolean delete (TokenCursor tokens){
        if (checkCache(tokens)) return true;
        var entity = tokens.nextToken();
        var initialEntity = entity;
        StatementType initialType = null;
        try {
             initialType = entity.toStatement().type;
        } catch (ClassCastException e) {
            Throw.syntaxAssert("DELETE " + entity.getName() , TokenType.STATEMENT, entity.tokenType);
        }
        switch (initialType) {
            case COLLECTION:
            case RELATIONSHIP: {
                StringBuilder parsedSoFar = new StringBuilder();
                String pathName = null;
                String collectionName = null;

                // Parse "DELETE COLLECTION" or "DELETE RELATIONSHIP"
                parsedSoFar.append("DELETE ").append(initialEntity.getName()).append(" ");

                // Parse the collection or relationship name
                entity = tokens.nextToken();
                parsedSoFar.append(entity.getName()).append(" ");
                Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STRING, entity.tokenType);
                collectionName = entity.getName();

                // Parse the "IN" keyword
                entity = tokens.nextToken();
                parsedSoFar.append(entity.getName()).append(" ");
                Throw.statementAssert(parsedSoFar.toString(), StatementType.IN, entity);

                // Parse the path
                entity = tokens.nextToken();
                parsedSoFar.append(entity.getName());
                Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STRING, entity.tokenType);
                pathName = entity.getName();

                // Construct args for the delete process
                HashMap<String, Object> args = new HashMap<>();
                if (initialType == StatementType.RELATIONSHIP) {
                    args.put("type", StoredProcedures.RELATIONSHIP);
                } else {
                    args.put("type", StoredProcedures.COLLECTION);
                }
                args.put("path", pathName);
                args.put("name", collectionName);

                processes.addProcess("delete", args);
                return true;
            }

            case ENTITY: {
                StringBuilder parsedSoFar = new StringBuilder();
                String collectionName = null;
                PathToken path = null;
                entity = tokens.nextToken();
                // verify the path keyword
                parsedSoFar.append("DELETE ").append(initialEntity.getName()).append(" ").append(entity.getName()).append(" ");
                Throw.statementAssert(parsedSoFar.toString(), StatementType.IN, entity);
                // verify that collection name exists
                entity = tokens.nextToken();
                parsedSoFar.append(entity.getName());
                Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STRING, entity.tokenType);
                collectionName = entity.name;
                HashMap<String, Object> args = new HashMap<>();
                args.put("type", StoredProcedures.ENTITY);
                args.put("path", collectionName);
                processes.addProcess("delete", args);
                return true;
            }
            case NODE: {
                String nodeName = null;
                entity = tokens.nextToken();
                // verify that collection name exists
                Throw.syntaxAssert("DELETE NODE " + entity.name, TokenType.STRING, entity.tokenType);
                nodeName = entity.getName();
                HashMap<String, Object> args = new HashMap<>();
                args.put("type", StoredProcedures.NODE);
                args.put("path", nodeName);
                processes.addProcess("delete", args);
                return true;
            }
            default: {
                throw Throw.invalidStatement("CREATE", initialType);
            }
        }
    }

    /*
    MATCH is special as it modifies the behavior of other clauses.
    For example, MATCH GET will return the result of the GET statement
    that matches the conditions by MATCH.

    Syntax:
    MATCH {CLAUSE} {CLAUSE ARGS} HAVING {PROPERTY} {OPERATOR} {VALUE}

        ex:
        for DELETION
        MATCH DELETE ENTITY "users" HAVING "name" = "John"

        for GET with REGEX
        MATCH GET "users.students" HAVING "name" LIKE "John"


     */

    // ARGS FOR MATCH
    // the args for the match-type
    // the args in the nested operation
    public boolean match (TokenCursor cursor) {
        ArrayList<Token> tokens = new ArrayList<>();
        while (true) {
            var currToken = cursor.nextToken();
            if (currToken instanceof Statement statement) {
                if (statement.type.equals(StatementType.HAVING)) {
                    break;
                }
            } tokens.add(currToken);
        }
        StringBuilder errormsg = new StringBuilder();
        errormsg.append("MATCH ");
        var subToken = new TokenCursor(tokens);
        Token operationToken = subToken.nextToken();
        Throw.syntaxAssert(errormsg.append(operationToken.getName()).toString(), TokenType.STATEMENT, operationToken.tokenType);
        StatementType operationType = operationToken.toStatement().type;

        // EXTRACT MATCH OPERATIONS
        // MATCH <> HAVING "" LIKE ""

        // assert the property token
        var propertyToken = cursor.nextToken();
        Throw.syntaxAssert(errormsg.append(propertyToken.getName()).toString(), TokenType.STRING, propertyToken.tokenType);
        // assert the operator token
        var operatorToken = cursor.nextToken();
        Throw.syntaxAssert(errormsg.append(operatorToken.getName()).toString(), TokenType.OPERATOR, operatorToken.tokenType);
        // assert the regexp token
        var regexpToken = cursor.nextToken();

        switch (operationType) {
            case GET: {
                // this is the GET OPERATION
                var args = parseGET(subToken);
                args.put("match-type", "GET");
                args.put("having", propertyToken.getName());
                args.put("operator", operatorToken.toOperator().OperatorType);
                args.put("value", regexpToken.getName());
                processes.addProcess("match", args);
            }
        }
        return true;
    }

    private HashMap<String, Object> parseGET(TokenCursor subToken) {
        var pathToken = subToken.nextToken();
        if (pathToken.tokenType == TokenType.ALIAS){
            HashMap<String, Object> args = new HashMap<>();
            args.put("alias", true);
            return args;
        }
        Throw.syntaxAssert("GET " + pathToken.getName(), TokenType.STRING, pathToken.tokenType);
        HashMap<String, Object> args = new HashMap<>();
        args.put("path", pathToken.getName());
        return args;
    }

}
