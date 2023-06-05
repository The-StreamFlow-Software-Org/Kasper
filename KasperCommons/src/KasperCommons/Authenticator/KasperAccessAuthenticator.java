package KasperCommons.Authenticator;

import KasperCommons.Exceptions.InvalidAccessKey;

public class KasperAccessAuthenticator {
    private static KasperAccessAuthenticator key;
    private static final String msg = "Your access key is invalid.\nPlease do not attempt to use locked methods in the KasperAPI as it may cause data inconsistencies.";

    public static KasperAccessAuthenticator getKey(){
        if (key == null) throw new InvalidAccessKey(msg);
        return key;
    }

    public KasperAccessAuthenticator (String keycode) {
        if (keycode.hashCode() != -50359679) throw new InvalidAccessKey(msg);
        key = this;
    }
}
