package parser.tokens;

import com.kasper.commons.datastructures.JSONUtils;
import com.kasper.commons.datastructures.KasperObject;
import parser.exceptions.Throw;

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
