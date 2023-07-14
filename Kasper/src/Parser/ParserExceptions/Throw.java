package Parser.ParserExceptions;

import KasperCommons.Exceptions.SyntaxError;

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
}
