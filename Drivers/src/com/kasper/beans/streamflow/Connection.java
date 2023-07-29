package com.kasper.beans.streamflow;

import com.kasper.commons.Network.NitroPacket;
import com.kasper.commons.exceptions.KasperException;

import java.io.IOException;
import java.net.Socket;

public class Connection {
    public NitroPacket packet;
    public Connection (String host, String username, String password, int port) {
        int retries = 0;
        boolean failed = true;
        while (failed && retries < 5) {
            try {
                packet = new NitroPacket(new Socket(host, port));
                failed = false;
            } catch (IOException e) {
                retries++;
            }
        } if (retries >=5) throw new KasperException("Cannot connect to Kasper Engine. Please check your connectivity.");
    }



    public Statement prepareStatement(String statement){
        return new Statement(this, statement);
    }
}
