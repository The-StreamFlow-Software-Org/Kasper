package KasperCommons.Exceptions;

public class InvalidAccessKey extends KasperRuntimeException {

    public InvalidAccessKey(String message) {
        super(message);
    }

    public InvalidAccessKey() {
    }
}
