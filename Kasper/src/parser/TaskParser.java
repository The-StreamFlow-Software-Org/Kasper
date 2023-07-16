package parser;

import parser.exceptions.Throw;
import parser.executor.ExecutionQueue;
import parser.tokens.PathToken;
import parser.tokens.StatementType;
import parser.tokens.TokenType;

public class TaskParser {
    ExecutionQueue processes;


    public TaskParser () {
        processes = new ExecutionQueue();
    }

    public Boolean create (TokenCursor tokens) {
        // CREATE statements here
        var entity = tokens.nextToken();
        var initialType = entity.toStatement().type;
        switch (initialType) {
            case RELATIONSHIP:
            case COLLECTION: {
                StringBuilder parsedSoFar = new StringBuilder();
                String collectionName = null;
                PathToken path = null;
                entity = tokens.nextToken();
                // verify that collection name exists
                parsedSoFar.append("CREATE COLLECTION ").append(entity.getName());
                Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STRING, entity.tokenType);
                collectionName = entity.name;
                entity = tokens.nextToken();
                parsedSoFar.append(" ").append(entity.name);
                // verify that IN exists
                Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STATEMENT, entity.tokenType);
                Throw.statementAssert(parsedSoFar.toString(), StatementType.IN, entity.toStatement().type);
                // verify that node name is provided
                entity = tokens.nextToken();
                parsedSoFar.append(" IN ").append(entity.name);
                Throw.syntaxAssert(parsedSoFar.toString(), TokenType.STRING, entity.tokenType);
                path = PathToken.newPath(entity.name);
                return true;
            }
            case NODE: {
                String nodeName = null;
                entity = tokens.nextToken();
                // verify that collection name exists
                Throw.syntaxAssert("CREATE COLLECTION " + entity.name, TokenType.STRING, entity.tokenType);
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
        Throw.statementAssert(parsedSoFar.toString(), StatementType.IN, tokens.nextToken().toStatement().type);
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

    public static Boolean get (TokenCursor tokens) {
        return false;
    }
}
