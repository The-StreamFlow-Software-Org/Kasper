package com.kasper.beans.nio.streamflow;

import com.kasper.beans.nio.protocol.Wire;
import com.kasper.commons.Network.NitroPacket;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.exceptions.KasperException;
import com.kasper.commons.exceptions.StreamFlowException;

import java.io.IOException;
import java.net.Socket;

public class Connection implements AutoCloseable {
    private long threadID = Thread.currentThread().getId();
    private Wire wire;
    public Connection (String host, String username, String password, int port) throws StreamFlowException {
        int retries = 0;
        boolean failed = true;
        while (failed && retries < 5) {
            try {
                this.wire = new Wire(new Socket(host, port), threadID);
                failed = false;
            } catch (IOException e) {
                retries++;
            }
        } if (retries >=5) throw new StreamFlowException("Cannot connect to Kasper Engine. Please check your connectivity.");
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
