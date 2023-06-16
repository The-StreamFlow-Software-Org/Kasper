package Kasper.BeansDriver.DataStructures;

import KasperCommons.DataStructures.KasperReference;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Network.NetworkPackage;

import java.net.Socket;

public class KasperBean extends AbstractReference{



    public KasperReference generateRawReference (String ref){
        return new KasperReference(ref);
    }



    public KasperBean(String host, String user, String password) throws KasperException {
        super("server");
        try {
            this.serverLocation = serverLocation;
            this.user = user;
            this.password = password;
            this.name = serverLocation;
            networkPackage = new NetworkPackage(new Socket(host, 53182));
        } catch (Exception e) {
            throw new KasperException(e.toString());
        }
    }

    public NodeReference useNode(String name){
        return new NodeReference(name, this);
    }



}