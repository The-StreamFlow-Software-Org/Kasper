package Kasper.BeansDriver.DataStructures;

import KasperCommons.Network.KasperNitroWire;

public abstract class AbstractReference {
    protected KasperNitroWire kasperNitroWire;
    protected String name;
    protected String type;
    protected String password;
    protected String user;
    protected String host;

    protected AbstractReference (String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }


}
