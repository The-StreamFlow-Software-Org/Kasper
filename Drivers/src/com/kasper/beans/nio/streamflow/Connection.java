package com.kasper.beans.nio.streamflow;

import com.kasper.beans.nio.protocol.Wire;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.exceptions.StreamFlowException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection implements AutoCloseable {
    private long threadID = Thread.currentThread().getId();
    protected Wire wire;
    public Connection (String host, String username, String password, int port) throws StreamFlowException {
        int retries = 0;
        boolean failed = true;
        while (failed && retries < 1) {
            this.wire = new Wire(new InetSocketAddress(host, port), threadID);
            failed = false;
        } if (retries >= 1) throw new StreamFlowException("Cannot connect to Kasper Engine. Please check your connectivity.");
        wire.authorization(username, password);
    }

    public Connection (String host, String username, String password) throws StreamFlowException {
        this(host, username, password, 53182);
    }
    private synchronized void authenticate () {
        KasperMap map = new KasperMap();
    }



    public Statement prepareStatement(String statement){
        return new Statement(this, statement);
    }

    @Override
    public void close() throws StreamFlowException {
        wire.close();
    }
}
