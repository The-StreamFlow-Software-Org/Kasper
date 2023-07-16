package parser.tokens;

public class StringLiteral extends Token{
    protected StringLiteral (String string) {
        this.name = string;
        this.tokenType = TokenType.STRING;
    }

    public static StringLiteral newLiteral (String string) {
        return new StringLiteral(string);
    }

    public PathToken toPath () {
        return PathToken.newPath(name);
    }
}
