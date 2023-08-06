package com.kasper.commons.exceptions;

public class KasperObjectAlreadyExists extends KasperException{
    public KasperObjectAlreadyExists(String message) {
        super(message);
    }

    public KasperObjectAlreadyExists(String type, String name, String parent) {
        super("The " + type + " '" + name + "' already exists in '" + parent + "'.");
    }
}
