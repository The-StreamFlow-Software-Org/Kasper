package com.kasper.commons.exceptions;

public class PreparedQueryException extends KasperException{

    public PreparedQueryException(String str) {
        super("Thrown by KasperBean[Streamflow]: Reason:> " + str);
    }


}
