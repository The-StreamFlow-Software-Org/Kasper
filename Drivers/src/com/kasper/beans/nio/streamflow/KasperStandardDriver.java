package com.kasper.beans.nio.streamflow;

import com.kasper.Boost.JSONCache;
import com.kasper.beans.nio.protocol.Wire;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.exceptions.StreamFlowException;

import java.net.InetSocketAddress;

public class KasperStandardDriver implements AutoCloseable, DriverInstance {

    /**
     *
     * @return the simple class name of the driver.
     */
    @Override
    public String getDriverType() {
        return "KasperStandardDriver";
    }

    private long threadID = Thread.currentThread().getId();
    protected Wire wire;


    protected KasperStandardDriver(String host, String username, String password, int port) throws StreamFlowException {
        JSONCache.init();
        int retries = 0;
        boolean failed = true;
        while (failed) {
            this.wire = new Wire(new InetSocketAddress(host, port), threadID);
            failed = false;
        }
        wire.authorization(username, password);
    }

    protected KasperStandardDriver(String host, String username, String password) throws StreamFlowException {
        this(host, username, password, 53182);
    }
    private synchronized void authenticate () {
        KasperMap map = new KasperMap();
    }


    /**
     *
     * @param statement this is the prepared query string that is to be sent to the server.
     * @return a Statement that holds the prepared query string.
     */
    public Statement prepareStatement(String statement){
        return new Statement(this, statement);
    }

    @Override
    public void close() throws StreamFlowException {
        wire.close();
    }

    @Override
    public void setTimeout(long timeout) {
        wire.setPacketTimeout(timeout);
    }
}
