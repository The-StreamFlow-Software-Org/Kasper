package Parser.Tokens;

public class OneOf extends Token{


    protected OneOf (TokenType type) {
        this.tokenType = type;
    }

    public static OneOf newOpenParen () {
        return new OneOf(TokenType.OPEN_PAREN);
    }

    public static OneOf newClosingParen() {
        return new OneOf(TokenType.CLOSING_PAREN);
    }

    public static OneOf newDelimiter(){
        return new OneOf(TokenType.DELIMITER);
    }

    public static OneOf newFunctionCall(){
        return new OneOf(TokenType.FUNCTION_CALL);
    }


}
