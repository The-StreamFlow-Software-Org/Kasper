package com.kasper.commons.datastructures;

public class KasperInteger extends KasperPrimitive {

    public KasperInteger(Integer data) {
        super("int");
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
