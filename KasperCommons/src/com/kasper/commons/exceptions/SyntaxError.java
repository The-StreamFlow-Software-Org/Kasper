package com.kasper.commons.exceptions;

public class SyntaxError extends KasperException{

    public SyntaxError (boolean empty, String message) {
        super (message);
    }
    public SyntaxError(String message) {
        super("Syntax Error thrown by the Kasper Engine.\nReason: " + message  + " Check the Kasper manual for details.");
    }

    public SyntaxError() {
    }
}
