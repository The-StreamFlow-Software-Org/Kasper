package KasperCommons.Aliases;

import KasperCommons.Authenticator.KasperCommons.Authenticator.PreparedPacket;

public class CommandAlias {
    PreparedPacket packet = new PreparedPacket();
    public static final int AUTH = 0;
    public static final int SET = 1;
    public static final int GET = 2;
    public static final int CREATE_NODE = 3;
    public static final int CREATE_COLLECTION = 4;
    public static final int EXISTS = 5;
    public static final int HAS_PROPERTY = 6;
    public static final int HAS_PROPERTY_EQUAL_TO = 7;
    public static final int DELETE = 8;
    public static final int UPDATE = 9;
    public static final int FULL_TEXT_SEARCH = 10;
    public static final int KASPER_NITRO_WIRE_ON = 11;
    public static final int AUTH_URI = 12;
    public static final int RESPONSE_OK = 13;
    public static final int RESPONSE_ERROR = 14;
    public static final int RESPONSE_ERROR_WITH_ARGS = 15;
    public static final int CLEAR = 16;
}
