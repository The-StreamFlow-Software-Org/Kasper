package com.kasper.commons.exceptions;

public class NonCollectionTypeException extends KasperException{
    public NonCollectionTypeException (String path) {
        super("Cannot insert in path '" + path + "', as it is of type [string/number/decimal]. Only [map] and [list] are iterable.");
    }
}
