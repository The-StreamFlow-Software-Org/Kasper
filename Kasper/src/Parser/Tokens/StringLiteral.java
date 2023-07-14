package Parser.Tokens;

public class StringLiteral extends Token{
    public String string;
    protected StringLiteral (String string) {
        this.string = string;
        this.tokenType = TokenType.STRING;
    }

    public static StringLiteral newLiteral (String string) {
        return new StringLiteral(string);
    }

    public PathToken toPath () {
        return PathToken.newPath(string);
    }
}
