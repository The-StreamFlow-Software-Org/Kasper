package parser;

import parser.exceptions.Throw;
import parser.executor.ExecutionQueue;
import parser.tokens.PathToken;
import parser.tokens.StatementType;
import parser.tokens.Token;
import parser.tokens.TokenType;

public class TaskParser {
    ExecutionQueue processes;


    public TaskParser () {
        processes = new ExecutionQueue();
    }

    public Boolean create (TokenCursor tokens) {
        var entity = tokens.nextToken();
        var initialEntity = entity;
        var initialType = entity.toStatement().type;
        switch (initialType) {
            case RELATIONSHIP:
            case COLLECTION: {
                StringBuilder parsedSoFar = new StringBuilder();
                String collectionName = null;
                PathToken path = null;
                entity = tokens.nextToken();
                // verify if the entity name exists
                parsedSoFar.append("CREATE ").append(initialEntity.getName()).append(" ").append(entity.getName()).append(" ");
                Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STRING, entity.tokenType);
                // verify the IN keyword
                entity = tokens.nextToken();
                parsedSoFar.append(entity.getName()).append(" ");
                Throw.statementAssert(parsedSoFar.toString(), StatementType.IN, entity);
                entity = tokens.nextToken();
                // get the path name here
                parsedSoFar.append(entity.getName());
                Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STRING, entity.tokenType);
                return true;
            }
            case NODE: {
                String nodeName = null;
                entity = tokens.nextToken();
                // verify that collection name exists
                Throw.syntaxAssert("CREATE NODE " + entity.name, TokenType.STRING, entity.tokenType);
                nodeName = entity.name;
                return true;
            }
            default: {
                throw Throw.invalidStatement("CREATE", initialType);
            }
        }
    }

    public Boolean insert (TokenCursor tokens) {
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
        return true;
    }

    public Boolean get (TokenCursor tokens) {
        var pathToken = tokens.nextToken();
        Throw.syntaxAssert("GET " + pathToken.getName(), TokenType.STRING, pathToken.tokenType);
        return true;
    }

    public Boolean delete (TokenCursor tokens){
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
                // verify in keyword
                entity = tokens.nextToken();
                parsedSoFar.append("DELETE ").append(initialEntity.getName()).append(" ").append(entity.getName()).append(" ");
                Throw.statementAssert(parsedSoFar.toString(), StatementType.IN, entity);
                // verify that collection name exists
                entity = tokens.nextToken();
                parsedSoFar.append(entity.getName());
                Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STRING, entity.tokenType);
                pathName = entity.name;
                //verify NAMED token
                entity = tokens.nextToken();
                parsedSoFar.append(" ").append(entity.getName());
                Throw.statementAssert(parsedSoFar.toString(), StatementType.NAMED, entity);
                // get relationship id
                entity = tokens.nextToken();
                parsedSoFar.append(" ").append(entity.getName());
                Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STRING, entity.tokenType);
                Token relationshipName = entity;
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
                Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STRING, entity.tokenType);
                collectionName = entity.name;
                return true;
            }
            case NODE: {
                String nodeName = null;
                entity = tokens.nextToken();
                // verify that collection name exists
                Throw.syntaxAssert("DELETE NODE " + entity.name, TokenType.STRING, entity.tokenType);
                nodeName = entity.name;
                return true;
            }
            default: {
                throw Throw.invalidStatement("CREATE", initialType);
            }
        }
    }


}
