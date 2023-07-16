package com.kasper.commons.exceptions;

public class InvalidAccessKey extends KasperRuntimeException {

    public InvalidAccessKey(String message) {
        super(message);
    }

    public InvalidAccessKey() {
    }
}
