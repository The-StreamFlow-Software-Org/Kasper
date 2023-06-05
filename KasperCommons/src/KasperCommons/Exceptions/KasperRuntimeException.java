package KasperCommons.Exceptions;

public class KasperRuntimeException extends RuntimeException{
    public KasperRuntimeException() {
    }

    public KasperRuntimeException(String message) {
        super(message);
    }
}
