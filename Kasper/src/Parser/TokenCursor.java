package Parser;

import Parser.ParserExceptions.Throw;
import Parser.Tokens.OneOf;
import Parser.Tokens.Statement;
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
        try {
            return innerList.get(cursor++);
        } catch (IndexOutOfBoundsException e) {
            throw Throw.raw("The query is incomplete, found upon parsing symbol: " + backToken().name );
        }
    }

    public TokenType peekNext () {
        try {
        return innerList.get(cursor).tokenType;
        } catch (IndexOutOfBoundsException e) {
            throw Throw.raw("The query is incomplete, found upon parsing symbol: " + backToken().name );
        }
    }

    public Token weakNextToken(){
        var tok = nextToken();
        cursor--;
        return tok;
    }

    public Token weakBackToken(){
        var tok = backToken();
        cursor++;
        return tok;
    }

    public TokenType peekBehind () {
        try {
        return innerList.get(cursor-1).tokenType;
        } catch (IndexOutOfBoundsException e) {
            throw Throw.raw("The query is incomplete, found upon parsing symbol: " + backToken().name );
        }
    }

    public Token backToken () {
        try {
            return innerList.get(--cursor);
        } catch (IndexOutOfBoundsException e) {
            if (innerList.size() == 0) throw Throw.raw("Token list is empty.");
            throw Throw.raw("The query is incomplete.");
        }
    }

    public boolean hasNext () {
        if (cursor < innerList.size()) return true;
        return false;
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
                toString.append(": ").append(x.toStringLiteral().name);
            } else if (x.tokenType==TokenType.OBJECT){
                toString.append(": ").append(x.toObject().getInternalObject());
            }
            toString.append('\n');
        }
        return toString.toString();
    }

    public String getString () {
        StringBuilder build = new StringBuilder();
        for (var x : innerList) {
            if (x instanceof OneOf) build.append(x.name);
            else if (x instanceof Statement statement) build.append(statement.type.toString()).append(" ");
            else
            build.append(x.name + " ");
        } return build.toString();
    }
}