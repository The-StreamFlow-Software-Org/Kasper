package Parser;

import Boost.Pair;
import KasperCommons.Exceptions.SyntaxError;
import Parser.ParserExceptions.Throw;
import Parser.Tokens.*;

import java.util.ArrayList;

public class ParseProcessor {

    public static void consumeString(String string) {
        try {
            var tokens = tokenize(string);
            parseSyntax(tokens); // verifies the correctness of the syntax
        } catch (SyntaxError error) {
            error.printStackTrace();
            throw new SyntaxError(true, error.getMessage() + "\nFound in query: '" + string + "'.");
        }
    }

    public static void parseSyntax (TokenCursor tokens) {
        boolean mustFinish = false; // the query must end, unless a delimiter was given.
        boolean begin = true; // this is a new query. Is false upon token read. Turned true with delimiters.
        while (tokens.hasNext()) {
            var current = tokens.nextToken();
            var type = current.tokenType;
            if (current.tokenType.equals(TokenType.DELIMITER)) {
                begin = true;
                mustFinish = false;
                continue;
            }
            if (begin) {
                Throw.syntaxAssert(tokens.getString(), TokenType.STATEMENT, tokens.peekBehind());
                begin = false;
            }
            if (mustFinish) {
                if (type.equals(TokenType.DELIMITER)) {
                    mustFinish = false;
                }
                else Throw.raw("Unknown start of query / symbol: " + current.name);
            }
            switch (type) {
                case STATEMENT -> {
                    switch (current.toStatement().type) {
                            case CREATE -> {
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
                                    mustFinish = true;
                                    break;
                                }
                                case NODE: {
                                    String nodeName = null;
                                    entity = tokens.nextToken();
                                    // verify that collection name exists
                                    Throw.syntaxAssert("CREATE COLLECTION " + entity.name, TokenType.STRING, entity.tokenType);
                                    nodeName = entity.name;
                                    mustFinish = true;
                                    break;
                                }
                                default: {
                                    throw Throw.invalidStatement("CREATE", initialType);
                                }
                            }
                        } case INSERT -> {
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
                        }
                    }
                }
            }
        }
    }

    public static TokenCursor tokenize (String longString) {
        ArrayList<Token> tokens = new ArrayList<>();
        for (int i = 0; i<longString.length(); i++){
            Token lastToken = null;
            if (i == 0) lastToken = OneOf.newInvalid();
            else lastToken = tokens.get(tokens.size()-1);
            char current = longString.charAt(i);

            // String literal handler
            // NOTE: String literals also include () as literals, but they are tokenized as Objects.
            switch (current) {
                case '\'':
                case '"':
                case '(':
                {
                    if (i == longString.length()-1) throw Throw.notEscaped(longString, current);
                    var pair = parseLiteral(longString.substring(i+1), current);
                    i = 0;
                    longString = pair.first();
                    if (current == '(') {
                        tokens.add(ObjectToken.newObject(pair.second()));
                    }
                    else tokens.add(StringLiteral.newLiteral(pair.second()));
                    break;
                }

                case ',': {
                    tokens.add(OneOf.newComma());
                    break;
                }

                case '@': {
                    tokens.add(OneOf.newFunctionCall());
                    break;
                }

                case ';': {
                    tokens.add(OneOf.newDelimiter());
                    break;
                }

                case '=':  {
                    tokens.add(OneOf.newEqual());
                    break;
                }

                case '*' : {
                    tokens.add(OneOf.newAlias());
                    break;
                }
                case '\n':
                case '\t':
                case ' ': break;
                default: {
                    StringBuilder statement = new StringBuilder();
                    current = longString.charAt(i);
                    do {
                        statement.append(current);
                        if (++i >= longString.length()) break;
                        current = longString.charAt(i);
                    }
                    while (current != ' ');
                    tokens.add(Statement.consumeStatement(statement.toString()));
                }

            }



        }
        return new TokenCursor(tokens);
    }

    public static Pair<String, String> parseLiteral (String literal, char delimiter) {
        if (delimiter == '(') delimiter = ')';
        StringBuilder string  = new StringBuilder();
        for (int i=0; i<literal.length(); i++) {
            char current = literal.charAt(i);
                // escape sequences
                if (current ==  '\\') {
                    i++;
                    current = literal.charAt(i);
                    switch (current) {
                        case '\'' -> {
                            if (delimiter == '\'') {
                                string.append('\'');
                            } else throw Throw.invalidEscape("\\'", "literal", literal);
                        }
                        case '"' -> {
                            if (delimiter == '"') {
                                string.append('"');
                            } else throw Throw.invalidEscape("\\\"", "literal", literal);
                        }
                        case ')' -> {
                            if (delimiter == ')') {
                                string.append(')');
                            } else throw Throw.invalidEscape("\\\"", "literal", literal);
                        }
                        case '.' -> {
                            string.append("\\.");
                        }
                        case '\\' ->
                            string.append('\\');
                    }
                } else if (current == delimiter) {
                    if (i+1 >= literal.length()) return new Pair<>("", string.toString());
                    return new Pair<>(literal.substring(i), string.toString());
            } else {
                    string.append(current);
                }
        } throw Throw.notEscaped(literal, delimiter);
    }


}
