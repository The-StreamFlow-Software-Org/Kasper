package com.kasper.beans.nio.streamflow;

import com.kasper.commons.exceptions.StreamFlowException;

public interface DriverInstance extends AutoCloseable {

    Statement prepareStatement (String statement);

    void close () throws StreamFlowException;
}
