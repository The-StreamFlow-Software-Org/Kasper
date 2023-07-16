package com.kasper.beans.discrete;

import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.exceptions.ResponseTypeInvalid;

public class StatusReport extends KasperObject {


    final String lazilyParsed;
    protected StatusReport (String type) {
        super("report");
        this.lazilyParsed = type;
    }

    public boolean isOk(){
        if (lazilyParsed.equals("OK")) return true;
        else if (lazilyParsed.equals("ERROR")) return false;
        else throw new ResponseTypeInvalid("ping", lazilyParsed);
    }

    public int getInt() {
        try {
            return Integer.parseInt(lazilyParsed);
        } catch (Exception e) {
            throw new ResponseTypeInvalid("integer", lazilyParsed);
        }
    }

}
