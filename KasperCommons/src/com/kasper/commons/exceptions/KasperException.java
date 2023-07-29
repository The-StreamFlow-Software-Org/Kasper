package com.kasper.commons.exceptions;


public class KasperException extends RuntimeException {

    public KasperException(String message) {
        super("Kasper Exception:> "+ message);
    }

    public KasperException (Exception e) {
        this(e.toString());
    }
    public KasperException(){

    }
}
