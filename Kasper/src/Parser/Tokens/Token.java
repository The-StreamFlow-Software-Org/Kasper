package Parser.Tokens;

public class Token {
    public TokenType tokenType;

    public Statement toStatement () {
        return (Statement) this;
    }

    public OneOf toOneOf() {
        return (OneOf) this;
    }

    public ObjectToken toObject () {
        return (ObjectToken) this;
    }

    public PathToken toPath () {
        return (PathToken) this;
    }

    public StringLiteral toStringLiteral () {return (StringLiteral) this; }
}
