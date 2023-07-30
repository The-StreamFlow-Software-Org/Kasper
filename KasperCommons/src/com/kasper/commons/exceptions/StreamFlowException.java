package com.kasper.commons.exceptions;

public class StreamFlowException extends Exception{
    public StreamFlowException(Throwable cause) {
        super(cause);
    }

    public StreamFlowException(String message) {
        super(message);
    }
}
