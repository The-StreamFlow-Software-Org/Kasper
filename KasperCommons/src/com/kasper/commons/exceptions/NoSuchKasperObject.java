package com.kasper.commons.exceptions;

public class NoSuchKasperObject extends KasperRuntimeException {
    public NoSuchKasperObject(String type, String name, String parent) {
        this("\nReason:> '" +  name + "' does not exist in " + type + " '" + parent + "'." );
    }

    public NoSuchKasperObject(String type, String name ) {
        this("\nReason:> " +  type + " '" + name + "' does not exist.");
    }

    public static String buildCommand(String type, String name ) {
        return "\nReason:> " +  type + " '" + name + "' does not exist.";
    }

    public static String buildCommand(String type, String name, String parent) {
        return "\nReason:> '" +  name + "' does not exist in " + type + " in path '" + parent + "'.";
    }

    public NoSuchKasperObject(String message) {
        super(message);
    }


}
