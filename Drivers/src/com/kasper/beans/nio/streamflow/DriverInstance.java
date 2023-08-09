package com.kasper.beans.nio.streamflow;

import com.kasper.commons.exceptions.StreamFlowException;

/**
 * A DriverInstance is a type of object that allows
 * database connections, specially to the StreamFlow/Kasper
 * family of data engines.
 */
public interface DriverInstance extends AutoCloseable {

    String getDriverType();

    /**
     * Prepares a statement before it is sent to the database engine.
     * @param statement the query string before its parameters are filled.
     * @return a Statement instance which can be used to fill in parameters.
     */
    Statement prepareStatement (String statement);


    /**
     * Closes all closeable attributes.
     * @throws StreamFlowException
     */
    void close () throws StreamFlowException;
}
