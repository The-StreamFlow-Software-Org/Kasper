package com.kasper.commons.datastructures;

import javax.swing.text.Document;

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
