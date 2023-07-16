package com.kasper.commons.exceptions;

public class ResponseTypeInvalid extends KasperException{

    public ResponseTypeInvalid (String type, String hint) {
        super("The response was not of " + type + " type. Hint on response: '" + hint + "'.");
    }
}
