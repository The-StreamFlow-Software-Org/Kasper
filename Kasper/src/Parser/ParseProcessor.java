package Parser;

import Boost.Pair;
import Parser.ParserExceptions.Throw;
import Parser.Tokens.Token;

import java.util.ArrayList;
import java.util.Map;

public class ParseProcessor {

    public static ArrayList<Token> tokenize (String longString) {
        ArrayList<Token> tokens = new ArrayList<>();
        for (int i = 0; i<longString.length(); i++){

        }
        return tokens;
    }

    public static Pair<String, String> parseLiteral (String literal, char delimiter) {
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
                        case '\\' ->
                            string.append('\\');
                    }
                } else if (current == delimiter) {
                    return new Pair<>(literal.substring(i + 1), string.toString());
            }
        } throw Throw.raw("The string literal " + literal + " was not escaped with the specified delimiter: '" + delimiter + "'.");
    }


}
