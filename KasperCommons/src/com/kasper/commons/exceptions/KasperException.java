package com.kasper.commons.exceptions;


import com.kasper.commons.datastructures.KasperEntity;
import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.datastructures.KasperString;

import java.util.Arrays;

public class KasperException extends RuntimeException implements KasperEntity {

    public KasperException(String message) {
        super(message);
    }

    public KasperException (Exception e) {
        setStackTrace(e.getStackTrace());
    }
    public KasperException(){

    }

    @Override
    public KasperObject getObject() {
        if (getMessage() == null) return new KasperString("Error not expected: " + getClass().getName() + " threw this, with super message: " + super.getMessage());
        return new KasperString(getMessage());
    }

    @Override
    public int getIntType() {
        return KasperEntity.TYPE_EXCEPTION;
    }
}
