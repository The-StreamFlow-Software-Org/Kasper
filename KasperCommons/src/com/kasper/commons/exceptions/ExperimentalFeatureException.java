package com.kasper.commons.exceptions;

public class ExperimentalFeatureException extends KasperException{

    public ExperimentalFeatureException(String method) {
        super("The feature '" + method + "' is an experimental feature and has been locked by the Kasper team.");
    }
}
