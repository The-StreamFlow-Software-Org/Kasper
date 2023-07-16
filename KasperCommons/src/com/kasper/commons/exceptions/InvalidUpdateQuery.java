package com.kasper.commons.exceptions;

public class InvalidUpdateQuery extends KasperRuntimeException{
    public InvalidUpdateQuery() {
    }

    public InvalidUpdateQuery(String name) {
        super("Kasper:> Cannot update " + name + " as this object is a higher-order object.\n" +
                "You can only update the keys in the collections. Collections and nodes cannot be updated.");
    }
}
