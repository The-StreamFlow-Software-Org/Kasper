package parser;

import com.kasper.Boost.Pair;
import com.kasper.commons.exceptions.SyntaxError;
import parser.exceptions.Throw;
import parser.tokens.*;

import java.util.ArrayList;

public class ParseProcessor {

    public void consumeString(String string) {
        try {
            var tokens = tokenize(string);
            parseSyntax(tokens); // verifies the correctness of the syntax
        } catch (SyntaxError error) {
            error.printStackTrace();
            throw new SyntaxError(true, error.getMessage() + "\nFound in query: '" + string + "'.");
        }
    }

    public void parseSyntax (TokenCursor tokens) {
        TaskParser processor = new TaskParser();
        Boolean mustFinish = false; // the query must end, unless a delimiter was given.
        Boolean begin = true; // this is a new query. Is false upon token read. Turned true with delimiters.
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
                else throw Throw.raw("Unknown start of query / symbol: '" + current.getName()  + "'.");
            }
            switch (current.toStatement().type) {
                case CREATE -> mustFinish = processor.create(tokens);
                case INSERT -> mustFinish = processor.insert(tokens);
                case GET -> mustFinish = processor.get(tokens);
                case DELETE -> mustFinish = processor.delete(tokens);
            }
        }
    }

    StringBuilder statement;
    ArrayList<Token> tokens;
    public void statementPusher() {
        if (!statement.isEmpty()) {
            tokens.add(Statement.consumeStatement(statement.toString()));
            statement.setLength(0);
        }
    }

    public TokenCursor tokenize (String longString) {
        tokens = new ArrayList<>();
        statement = new StringBuilder();
        for (int i = 0; i<longString.length(); i++){
            char current = longString.charAt(i);

            // String literal handler
            // NOTE: String literals also include () as literals, but they are tokenized as Objects.
            switch (current) {
                case '\'':
                case '"':
                case '(':
                {
                    statementPusher();
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
                    statementPusher();
                    tokens.add(OneOf.newComma());
                    break;
                }

                case '@': {
                    statementPusher();
                    tokens.add(OneOf.newFunctionCall());
                    break;
                }

                case ';': {
                    statementPusher();
                    tokens.add(OneOf.newDelimiter());
                    break;
                }
                case '=':  {
                    statementPusher();
                    tokens.add(OneOf.newEqual());
                    break;
                }

                case '*' : {
                    statementPusher();
                    tokens.add(OneOf.newAlias());
                    break;
                }
                case '\n':
                case '\t':
                case ' ': {
                    statementPusher();
                    break;
                }
                default: {
                    statement.append(current);
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
