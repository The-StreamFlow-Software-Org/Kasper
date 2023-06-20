package KasperCommons.Exceptions;

public class NoSuchKasperCommandException extends KasperException{

    public NoSuchKasperCommandException(String message) {
        super(message);
    }

    public NoSuchKasperCommandException() {
        super();
    }
}
