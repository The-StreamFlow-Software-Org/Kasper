package parser.exceptions;

import com.kasper.commons.datastructures.*;
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

    public static void notDelimited() {
        throw new SyntaxError("The query had excess tokens. Must be delimited with a semicolon ';' if a query must finish.");
    }

    public static void assertOperatorValidity(String operator, KasperObject object) {
        if (!(object instanceof KasperPrimitive)) throw new SyntaxError("Cannot compare non-primitive types. You supplied a " + object.getType() + " object.");
        if (object instanceof KasperInteger || object instanceof KasperDecimal) return;
        if (operator.equals("=") || operator.equals("!=")) return;
        throw new SyntaxError("The operator '" + operator + "' is invalid when comparing a " + object.getType()+ " object.");
    }

    public static void typeChecking (KasperObject object, Object other) {
        if (object.getClass().equals(other.getClass()));
        else throw new SyntaxError("Cannot compare a " + object.getClass().getSimpleName() + " object with a " + other.getClass().getSimpleName() + " object.");
    }
}
