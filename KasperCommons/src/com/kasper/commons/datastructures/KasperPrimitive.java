package com.kasper.commons.datastructures;

import java.io.Serial;

/**
 * The most basic of the Kasper data structures,
 * it is a primitive data structure that holds a string.
 */
public abstract class KasperPrimitive extends KasperObject{

    @Serial
    private static final long serialVersionUID = 7590033187664945326L;

    public KasperPrimitive(String type) {
        super(type);
    }

    @Override
    public String toString() {
        return toStr();
    }

    public abstract boolean equals(Object obj);
}
