package com.kasper.beans.datastructures;

import com.kasper.commons.exceptions.BeanConcurrencyException;
import com.kasper.commons.Network.KasperNitroWire;

import java.io.IOException;

public abstract class AbstractReference {
    protected static boolean EXPERIMENTAL_MODE = false;
    protected KasperNitroWire kasperNitroWire;
    protected String name;
    protected String type;
    protected String password;
    protected String user;
    protected String host;

    protected AbstractReference (String type) {
        this.type = type;
    }
    protected long threadID;

    protected void verifyConcurrency() {
        if (Thread.currentThread().getId() != threadID)
            throw new BeanConcurrencyException();
    }
    protected void inheritThreadFromParent(AbstractReference parent) {
        this.threadID = parent.threadID;
    }

    public String getName() {
        return name;
    }

    public void close () throws IOException {
        kasperNitroWire.close();
    }


}
