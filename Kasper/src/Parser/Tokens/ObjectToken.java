package Parser.Tokens;

import KasperCommons.DataStructures.JSONUtils;
import KasperCommons.DataStructures.KasperObject;
import Parser.ParserExceptions.Throw;

public class ObjectToken extends Token{
    protected KasperObject internalObject;

    public static ObjectToken newObject (String str) {
        var thisToken = new ObjectToken();
        thisToken.name = str;
        thisToken.tokenType = TokenType.OBJECT;
        try {
            thisToken.internalObject = JSONUtils.parseJson(str);
        } catch (Exception e) {
            throw Throw.raw("The object '" + str + "' cannot be parsed. The format is invalid.");
        } return thisToken;
    }

    public KasperObject getInternalObject () {
        return internalObject;
    }
}
