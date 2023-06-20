package KasperCommons.Exceptions;

public class KasperObjectAlreadyExists extends KasperException{
    public KasperObjectAlreadyExists(String message) {
        super(message);
    }

    public KasperObjectAlreadyExists() {
        super();
    }
}
