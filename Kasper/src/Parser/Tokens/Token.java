package Parser.Tokens;

public class Token {

    public String name;
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

    public String getName () {
        if (this instanceof Statement s) return s.type.toString();
        return name;
    }
}
