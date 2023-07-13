package KasperCommons.Exceptions;

public class SyntaxError extends KasperException{
    public SyntaxError(String message) {
        super("Syntax Error thrown by the Kasper Engine.\nReason: " + message  + " Check the Kasper manual for details.");
    }

    public SyntaxError() {
    }
}
