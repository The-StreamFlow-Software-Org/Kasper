package KasperCommons.Authenticator;

import KasperCommons.Exceptions.InvalidAccessKey;

public class KasperAccessAuthenticator {
    private static KasperAccessAuthenticator key;
    private static final String msg = "Your access key is invalid.\nPlease do not attempt to use locked methods in the KasperAPI as it may cause data inconsistencies.";

    public static KasperAccessAuthenticator getKey(){
        if (key == null) throw new InvalidAccessKey(msg);
        return key;
    }

    public KasperAccessAuthenticator (Object keycode) {
        if (keycode.hashCode() != getHashedKey()) throw new InvalidAccessKey(msg);
        key = this;
    }

    public static int getHashedKey () {
        return -50359679;
    }
}
