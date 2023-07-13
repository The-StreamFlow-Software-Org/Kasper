package Parser.Tokens;

import KasperCommons.DataStructures.JSONUtils;
import KasperCommons.DataStructures.KasperObject;
import Parser.ParserExceptions.Throw;

public class ObjectToken extends Token{
    protected KasperObject internalObject;

    public static ObjectToken newObject (String str) {
        var thisToken = new ObjectToken();
        thisToken.tokenType = TokenType.OBJECT;
        try {
            thisToken.internalObject = JSONUtils.parseJson(str);
        } catch (Exception e) {
            throw Throw.raw("Invalid object format in query, flagged with the exception '" + e.getMessage() + "'.");
        } return thisToken;
    }
}
