package Parser;

import Parser.Tokens.Token;
import Parser.Tokens.TokenType;

import java.util.ArrayList;

public class TokenCursor {
    private ArrayList<Token> innerList;
    int cursor;

    protected TokenCursor(ArrayList<Token> token) {
        this.innerList = token;
        cursor = 0;
    }

    public Token nextToken () {
        return innerList.get(cursor++);
    }

    public TokenType peekNext () {
        return innerList.get(cursor).tokenType;
    }

    public TokenType peekBehind () {
        return innerList.get(cursor-1).tokenType;
    }

    public Token backToken () {
        return innerList.get(--cursor);
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        for (var x : innerList) {
            toString.append(x.tokenType.toString());
            if (x.tokenType==TokenType.STATEMENT) {
                toString.append(": ").append(x.toStatement().name);
            } else if (x.tokenType==TokenType.PATH) {
                toString.append(": ").append(x.toPath().path);
            } else if (x.tokenType==TokenType.STRING) {
                toString.append(": ").append(x.toStringLiteral().string);
            }
            toString.append('\n');
        }
        return toString.toString();
    }
}