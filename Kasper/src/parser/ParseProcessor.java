package parser;

import com.kasper.Boost.Pair;
import com.kasper.commons.datastructures.JSONUtils;
import com.kasper.commons.datastructures.KasperList;
import com.kasper.commons.debug.Debug;
import com.kasper.commons.exceptions.SyntaxError;
import nio.kasper.StagedResultSet;
import parser.exceptions.Throw;
import parser.executor.ExecutionQueue;
import parser.tokens.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class ParseProcessor {

    public void executeQuery(String str, StagedResultSet resultSet) {
        consumeString(str);
    }

    public void consumeString(String string) {
        try {
            var tokens = tokenize(string);
            for (var tokenCursors : tokens) {
                parseSyntax(tokenCursors);
            } // verifies the correctness of the syntax
            if (Debug.TRUE) System.out.println("Syntax verified: " + string);
        } catch (SyntaxError error) {
            if (Debug.TRUE)error.printStackTrace();
            throw new SyntaxError(true, error.getMessage() + "\n\tFound in query: '" + string + "'.");
        }
    }

    public ExecutionQueue parseSyntax (TokenCursor tokens) {
        TaskParser processor = new TaskParser();
        Boolean mustFinish = false; // the query must end, unless a delimiter was given.
        Boolean begin = true; // this is a new query. Is false upon token read. Turned true with delimiters.
        while (tokens.hasNext()) {
            var current = tokens.nextToken();
            var type = current.tokenType;
            if (mustFinish) {
                Throw.notDelimited();
            }
            if (current.tokenType.equals(TokenType.DELIMITER)) {
                begin = true;
                mustFinish = false;
                continue;
            }
            if (begin) {
                Throw.syntaxAssert(tokens.getString(), TokenType.STATEMENT, tokens.peekBehind());
                begin = false;
            }
            if (current instanceof FunctionToken) {
                // do something here
            }

            else {
                if (!(current instanceof Statement)) {
                    throw Throw.raw("Unknown start of query / symbol: '" + current.getName() + "'.");
                }
                switch (current.toStatement().type) {
                    case CREATE -> mustFinish = processor.create(tokens);
                    case INSERT -> mustFinish = processor.insert(tokens);
                    case GET -> mustFinish = processor.get(tokens);
                    case DELETE -> mustFinish = processor.delete(tokens);
                    default -> throw Throw.raw("Unknown start of query / symbol: '" + current.getName() + "'.");
                }
            }
        }
        return processor.getExecutionQueue();
    }

    StringBuilder statement;
    ArrayList<Token> tokens;
    public void statementPusher() {
        if (!statement.isEmpty()) {
            tokens.add(Statement.consumeStatement(statement.toString()));
            statement.setLength(0);
        }
    }

    public ArrayList<TokenCursor> tokenize (String longString) {
        tokens = new ArrayList<>();
        var tokenCursors = new ArrayList<TokenCursor>();
        statement = new StringBuilder();
        for (int i = 0; i<longString.length(); i++){
            char current = longString.charAt(i);
            switch (current) {
                case '\'':
                case '"':
                {
                    statementPusher();
                    if (i == longString.length()-1) throw Throw.notEscaped(longString, current);
                    var pair = parseLiteral(longString.substring(i+1), current);
                    i = 0;
                    longString = pair.first();
                    tokens.add(StringLiteral.newLiteral(pair.second()));
                    --i;
                    break;
                }
                case '(': {
                    statementPusher();
                    var pair = parseParentheses(longString.substring(i));
                    i = 0;
                    longString = pair.first();
                    Token prev = null;
                    try {
                        prev = tokens.get(tokens.size() - 1);
                        if (prev.tokenType.equals(TokenType.FUNCTION)) {
                            var func = (FunctionToken) prev;
                            func.injectArguments((KasperList) JSONUtils.parseJson('[' + pair.second() + ']'));
                        } else
                        tokens.add(ObjectToken.newObject(pair.second()));
                    } catch (IndexOutOfBoundsException e ) {
                        throw Throw.raw("Unexpected '(' found in query. Expected a function call or a literal.");
                    } catch (Exception e) {
                        throw Throw.raw("Invalid argument list for function call '" + prev.getName() + "'.");
                    } finally {
                        break;
                    }
                }

                case ',': {
                    statementPusher();
                    tokens.add(OneOf.newComma());
                    break;
                }

                case '@': {
                    statementPusher();
                    StringBuilder builder = new StringBuilder();
                    boolean goOn = true;
                    while (goOn) {
                        if (++i >= longString.length())
                            throw Throw.raw("Unexpected unterminated function call, expected ')' or space at the end of the function.\n\tFound while parsing the function name: \n\t\t'" + builder +"'");
                        current = longString.charAt(i);
                        switch (current) {
                            case '(':
                                --i;
                                goOn = false;
                                break;
                            case ' ':
                                goOn = false;
                                break;
                            default:
                                if (Character.isAlphabetic(current)) {
                                    builder.append(current);
                                } else {
                                    throw Throw.raw("Unexpected character '" + current + "' in function name. Expected an alphabetic character.");
                                }
                        }
                    }

                    tokens.add(FunctionToken.newFunction(builder.toString()));
                    break;
                }

                case ';': {
                    statementPusher();
                    // Add the current statement to tokenCursors
                    if (tokens.size() > 0) {
                        tokenCursors.add(new TokenCursor(new ArrayList<>(tokens)));
                        tokens = new ArrayList<>();
                    }
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

        if (statement.length() > 0) {
            statementPusher();
        }

        // Handle the last statement (if it doesn't end with a ';')
        if (tokens.size() > 0) {
            tokenCursors.add(new TokenCursor(tokens));
        }
        return tokenCursors;
    }

    public static Pair<String, String> parseParentheses(String str) {
        Stack<Integer> stack = new Stack<>();
        StringBuilder insideParentheses = new StringBuilder();
        boolean insideString = false;
        char stringDelimiter = '\"'; // default string delimiter

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (!insideString && c == '(') {
                if (stack.isEmpty()) {
                    // reset the content when a new parentheses block is encountered
                    insideParentheses = new StringBuilder();
                }
                stack.push(i);
            } else if (!insideString && c == ')') {
                if (stack.isEmpty()) {
                    throw Throw.raw("Invalid parentheses structure when parsing: '" + str + "'.");
                }
                stack.pop();
                if (stack.isEmpty()) {
                    // found a matching pair of parentheses, break the loop
                    break;
                }
            } else if (c == '\"') {
                if (!insideString) {
                    // starting a new string
                    insideString = true;
                    stringDelimiter = c;
                    insideParentheses.append('\"'); // always use " as the string delimiter in the result
                } else if (str.charAt(i - 1) != '\\' || (str.charAt(i - 1) == '\\' && str.charAt(i - 2) == '\\')) {
                    // ending the string, taking escape sequences into account
                    insideString = c != stringDelimiter || str.charAt(i - 1) == '\\';
                    insideParentheses.append('\"'); // always use " as the string delimiter in the result
                } else {
                    // inside the string, append as is
                    insideParentheses.append(c);
                }
            } else if (c == '\\' && (i + 1) < str.length() && (str.charAt(i + 1) == '\'' || str.charAt(i + 1) == '\"')) {
                // handle escape sequences
                if (str.charAt(i + 1) == stringDelimiter) {
                    // valid escape sequence, append as is
                    insideParentheses.append(c).append(str.charAt(i + 1));
                    i++; // skip next char
                } else {
                    throw Throw.raw("Invalid escape sequence when parsing: '" + str + "'.");
                }
            } else if (!stack.isEmpty()) {
                // inside parentheses, append char as is
                insideParentheses.append(c);
            }
        }

        if (!stack.isEmpty()) {
            throw Throw.raw("Invalid parentheses structure when parsing: '" + str + "'.");
        }

        String afterParentheses = str.substring(insideParentheses.length() + 2); // add 2 to account for the parentheses
        return new Pair<>(afterParentheses, insideParentheses.toString());
    }


    public Pair<String, String> parseLiteral (String literal, char delimiter) {
        if (delimiter == '(') delimiter = ')';
        StringBuilder string  = new StringBuilder();
        boolean isEscaping = false; // New flag to track escape sequences
        for (int i=0; i<literal.length(); i++) {
            char current = literal.charAt(i);
            if (isEscaping) {
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
                        } else throw Throw.invalidEscape("\\)", "literal", literal);
                    }
                    case '.' -> {
                        string.append(".");
                    }
                    case '\\' -> {
                        string.append('\\');
                    }
                    default -> {
                        string.append(current);
                    }
                }
                isEscaping = false;
            } else if (current == '\\') {
                isEscaping = true;
            } else if (current == delimiter) {
                if (i+1 >= literal.length()) return new Pair<>("", string.toString());
                return new Pair<>(literal.substring(i + 1), string.toString());
            } else {
                string.append(current);
            }
        }
        throw Throw.notEscaped(literal, delimiter);
    }


}
