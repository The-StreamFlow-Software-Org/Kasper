package KasperCommons.Exceptions;

public class NoSuchKasperObject extends KasperRuntimeException {
    public NoSuchKasperObject() {
        super();
    }

    public NoSuchKasperObject(String message) {
        super(message);
    }
}
