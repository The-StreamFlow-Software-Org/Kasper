package com.kasper.commons.exceptions;

public class PreparedQueryException extends StreamFlowException{

    public PreparedQueryException(String str) {
        super("\nThrown by KasperBean[Streamflow]: Reason:> " + str);
    }


}
