package com.kasper.beans.discrete;

import com.kasper.commons.datastructures.KasperList;
import com.kasper.commons.datastructures.KasperObject;

import java.util.ArrayList;

public class Cursor {
    ArrayList<KasperObject> internalObjects;


    protected Cursor (KasperList unparsedList) {
        /*
        TODO: make the resultSet adhere to the following:
        either:
        ["exception, "exception_msg"]
        ["result", ["status", "status", "result"], ["ok", "ok", {}]]
         */
    }
    public static StatusReport newStatus(String statusReport){
        return new StatusReport(statusReport);
    }

}
