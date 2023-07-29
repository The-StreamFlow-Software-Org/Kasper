package com.kasper.beans.nio.streamflow;

import com.kasper.beans.nio.protocol.Wire;
import com.kasper.commons.Network.NitroPacket;
import com.kasper.commons.authenticator.Meta;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.debug.W;
import com.kasper.commons.exceptions.KasperException;

import java.io.IOException;
import java.net.Socket;

public class Connection {
    public NitroPacket packet;
    private static long threadID = Thread.currentThread().getId();
    private Wire wire;
    public Connection (String host, String username, String password, int port) {
        int retries = 0;
        boolean failed = true;
        while (failed && retries < 5) {
            try {
                packet = new NitroPacket(new Socket(host, port));
                this.wire = new Wire(packet.socket(), threadID);
                failed = false;
            } catch (IOException e) {
                retries++;
            }
        } if (retries >=5) throw new KasperException("Cannot connect to Kasper Engine. Please check your connectivity.");
        wire.authorization(username, password);
    }

    public Connection (String host, String username, String password) {
        this(host, username, password, 53182);
    }
    private synchronized void authenticate () {
        KasperMap map = new KasperMap();
    }



    public Statement prepareStatement(String statement){
        return new Statement(this, statement);
    }
}
