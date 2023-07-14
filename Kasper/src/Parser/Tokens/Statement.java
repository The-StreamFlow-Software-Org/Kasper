package Parser.Tokens;

import Parser.ParserExceptions.Throw;

public class Statement extends Token {
    public StatementType type;
    public String name;

    public static Statement consumeStatement (String statement) {
        try {
            var type = StatementType.identify(statement);
            return new Statement(statement, type);
        } catch (IllegalArgumentException e) {
            throw Throw.invalidStatement(statement);
        }
    }

    protected Statement (String statement, StatementType type) {
        this.name = statement;
        this.type = type;
        this.tokenType = TokenType.STATEMENT;
    }

}
