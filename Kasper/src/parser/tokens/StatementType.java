package parser.tokens;

public enum StatementType {
    CREATE, RELATE, GET, MATCH, IN, AS, TO, INSERT, DELETE, UPDATE,
    NODE, COLLECTION, RELATIONSHIP, HAVING, ENTITY, NAMED, INCLUDE,
    ASSERT;

    public static StatementType identify (String str) {
        return StatementType.valueOf(str.toUpperCase());
    }

}
