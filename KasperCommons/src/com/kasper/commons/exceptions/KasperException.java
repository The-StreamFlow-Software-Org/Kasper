package com.kasper.commons.exceptions;


import com.kasper.commons.datastructures.KasperEntity;
import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.datastructures.KasperString;

public class KasperException extends RuntimeException implements KasperEntity {

    public KasperException(String message) {
        super("Kasper Exception:> "+ message);
    }

    public KasperException (Exception e) {
        this(e.toString());
    }
    public KasperException(){

    }

    @Override
    public KasperObject getObject() {
        return new KasperString(getMessage());
    }

    @Override
    public int getIntType() {
        return KasperEntity.TYPE_EXCEPTION;
    }
}
