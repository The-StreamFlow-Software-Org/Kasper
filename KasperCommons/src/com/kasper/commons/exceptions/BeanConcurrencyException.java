package com.kasper.commons.exceptions;

public class BeanConcurrencyException extends KasperException{
    public BeanConcurrencyException() {
        super("Thrown by KasperEngine: Reason:> KasperBean is not made for concurrent access\nYou can only use a bean in the thread it was instantiated in.");
    }
}
