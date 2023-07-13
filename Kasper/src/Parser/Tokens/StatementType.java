package Parser.Tokens;

import java.util.HashMap;

public enum StatementType {
    CREATE, RELATE, GET, MATCH, IN, AS, TO, INSERT, EQUAL, DELETE, UPDATE,
    NODE, COLLECTION, RELATIONSHIP;

    public static StatementType identify (String str) {
        return StatementType.valueOf(str.toUpperCase());
    }

}
