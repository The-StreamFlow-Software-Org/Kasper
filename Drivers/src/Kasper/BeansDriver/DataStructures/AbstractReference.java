package Kasper.BeansDriver.DataStructures;

import KasperCommons.DataStructures.KasperObject;

public abstract class AbstractReference {
    protected String name;
    protected String type;
    protected String password;
    protected String user;
    protected String serverLocation;


    protected AbstractReference (String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }
}
