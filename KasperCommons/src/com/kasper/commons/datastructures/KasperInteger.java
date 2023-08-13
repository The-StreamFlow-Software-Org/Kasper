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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KasperInteger integerInstance) {
            return ((Integer)data).equals(integerInstance.data);
        } else return data.equals(obj);
    }
}
