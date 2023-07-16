package com.kasper.commons.datastructures;

import java.io.Serial;

/**
 * The most basic of the Kasper data structures,
 * it is a primitive data structure that holds a string.
 */
public class KasperString extends KasperObject{

    @Serial
    private static final long serialVersionUID = 7590033187664945326L;

    public KasperString(String data) {
        super("string");
        this.data = data.intern();
    }

    @Override
    public String toString() {
        return toStr();
    }
}
