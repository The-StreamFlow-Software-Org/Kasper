package Kasper.BeansDriver.DataStructures;

import KasperCommons.Exceptions.KasperException;

public class KasperBean extends AbstractReference{

    public KasperBean(String host, String user, String password) throws KasperException {
        super("server");
        this.serverLocation = serverLocation;
        this.user = user;
        this.password = password;
        this.name = serverLocation;
    }

    public NodeReference useNode (String nodeName){
        return new NodeReference(nodeName, this);
    }


}