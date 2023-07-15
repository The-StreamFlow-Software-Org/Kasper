package Parser.Tokens;

import Parser.ParserExceptions.Throw;

public class Statement extends Token {
    public StatementType type;
    public String name;

    public static Statement consumeStatement (String statement) {
        try {
            var type = StatementType.identify(statement);
            return new Statement(type);
        } catch (IllegalArgumentException e) {
            throw Throw.invalidStatement(statement);
        }
    }

    protected Statement (StatementType type) {
        this.name = type.toString();
        this.type = type;
        this.tokenType = TokenType.STATEMENT;
    }

}
