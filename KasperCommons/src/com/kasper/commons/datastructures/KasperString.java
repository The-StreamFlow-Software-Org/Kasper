package com.kasper.commons.datastructures;

public class KasperString extends KasperPrimitive{
    public KasperString(String data) {
        super("string");
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KasperString stringInstance) {
            return stringInstance.toStr().equals(data);
        } else return data.equals(obj);
    }
}
