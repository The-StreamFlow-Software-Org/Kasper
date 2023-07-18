package parser.exceptions;

import com.kasper.commons.exceptions.SyntaxError;
import parser.tokens.StatementType;
import parser.tokens.Token;
import parser.tokens.TokenType;

public class Throw {

    public static SyntaxError invalidStatement(String statement) throws SyntaxError {
       return new SyntaxError("Statement '" + statement + "' is not a legal statement.");
    }

    public static SyntaxError invalidEscape(String sequence, String type, String context) throws SyntaxError {
        return new SyntaxError("The escape sequence '" + sequence + "' is invalid. This was found while parsing the " + type + " '"  + context + "'.");
    }

    public static SyntaxError raw (String sequence) {
        return new SyntaxError(sequence);
    }

    public static SyntaxError notEscaped (String literal, char delimiter) {
        return new SyntaxError("The string literal " + literal + " was not escaped with the specified delimiter: '" + delimiter + "'.");
    }

    public static void syntaxAssert (String sequence, TokenType expected, TokenType received) {
        if (expected != received)
        throw raw("Syntax assertion failed during the parsing of the last token of the sequence:\n\t\t\"" + sequence + "\"\n\tThe engine expected '" + expected + "', but instead, received '" + received + "'. ");
    }

    private static void statementAssert (String sequence, StatementType expected, StatementType received) {
        if (expected != received)
            throw raw("Statement assertion failed.\n\tSequence '" + sequence + "' is incorrect.\n\tDetails: expected statement '" + expected + "' received '" + received + "'.");
    }

    public static void statementAssert (String sequence, StatementType expected, Token received) {
        try {
            syntaxAssert(sequence, TokenType.STATEMENT, received.tokenType);
        } catch (SyntaxError e) {
            throw raw("Syntax assertion failed during the parsing of the last token of the sequence:\n\t\t\"" + sequence + "\"\n\tThe engine expected '" + expected.toString() + "', but instead, received '" + received.getName() + "'. ");
        }
        statementAssert(sequence, expected, received.toStatement().type);
    }

    public static SyntaxError invalidStatement (String initial, StatementType type) {
        return new SyntaxError("Cannot parse statement '" + type + "' after initial statement '" + initial + "'.");
    }
}
