package Kasper.BeansDriver.DataStructures;

import KasperCommons.Exceptions.BeanConcurrencyException;
import KasperCommons.Network.KasperNitroWire;

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
        if (Thread.currentThread().threadId() != threadID)
            throw new BeanConcurrencyException();
    }
    protected void inheritThreadFromParent(AbstractReference parent) {
        this.threadID = parent.threadID;
    }

    public String getName() {
        return name;
    }


}
