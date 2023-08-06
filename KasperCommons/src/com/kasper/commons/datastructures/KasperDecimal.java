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
}
