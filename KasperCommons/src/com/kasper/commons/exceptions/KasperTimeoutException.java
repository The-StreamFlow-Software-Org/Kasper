package com.kasper.commons.exceptions;

public class KasperTimeoutException extends KasperException{
    public KasperTimeoutException() {
        super("The server timed out.");
    }
}
