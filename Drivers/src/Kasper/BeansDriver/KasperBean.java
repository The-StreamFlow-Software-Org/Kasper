package Kasper.BeansDriver;

import KasperCommons.Exceptions.KasperException;
import org.w3c.dom.Node;

public class KasperBean extends AbstractReference{

    public KasperBean(String serverLocation, String user, String password) throws KasperException {
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