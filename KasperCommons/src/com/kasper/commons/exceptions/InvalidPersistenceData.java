package com.kasper.commons.exceptions;

public class InvalidPersistenceData extends KasperException{

    public InvalidPersistenceData(String message) {
        super(message);
    }

    public InvalidPersistenceData() {
        super();
    }
}
