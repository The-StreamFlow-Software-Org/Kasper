package Parser.ParserExceptions;

import KasperCommons.Exceptions.SyntaxError;
import Parser.Tokens.Statement;
import Parser.Tokens.StatementType;
import Parser.Tokens.Token;
import Parser.Tokens.TokenType;

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
        throw raw("Syntax assertion failed. The sequence:\n\t\"" + sequence + "\"\n\tThe engine expected '" + expected + "', but instead, received '" + received + "'. ");
    }

    public static void statementAssert (String sequence, StatementType expected, StatementType received) {
        if (expected != received)
            throw raw("Statement assertion failed.\n\tSequence '" + sequence + "' is incorrect.\n\tDetails: expected statement '" + expected + "' received '" + received + "'.");
    }

    public static void statementAssert (String sequence, StatementType expected, Token received) {
        syntaxAssert(sequence, TokenType.STATEMENT, received.tokenType);
        statementAssert(sequence, expected, received.toStatement().type);
    }

    public static SyntaxError invalidStatement (String initial, StatementType type) {
        return new SyntaxError("Cannot parse statement '" + type + "' after initial statement '" + initial + "'.");
    }
}
