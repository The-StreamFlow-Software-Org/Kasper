package com.kasper.commons.datastructures;

public class KasperDecimal extends KasperPrimitive{

    public KasperDecimal(Double data) {
        super("decimal");
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KasperDecimal doubleInstance) {
            return data.equals(doubleInstance.data);
        } else return data.equals(obj);
    }
}
