package KasperCommons.Exceptions;

public class KasperPathException extends KasperException{

    public KasperPathException() {
        super("Reason:> Cannot deduce paths not loaded from KasperEngine.");
    }
}
