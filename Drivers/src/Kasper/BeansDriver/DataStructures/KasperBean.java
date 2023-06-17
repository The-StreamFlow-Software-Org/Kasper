package Kasper.BeansDriver.DataStructures;

import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperReference;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Network.NetworkPackage;
import KasperCommons.Parser.KasperWriter;
import KasperCommons.Parser.PathParser;

import java.io.IOException;
import java.net.Socket;

public class KasperBean extends AbstractReference{



    public KasperReference generateRawReference (String ref){
        return new KasperReference(ref);
    }



    public KasperBean(String host, String user, String password) throws KasperException {
        super("server");
        new KasperAccessAuthenticator("kasper.util.key");
        try {
            this.host = host;
            this.user = user;
            this.password = password;
            this.name = this.host;
            networkPackage = new NetworkPackage(new Socket(host, 53182));
        } catch (Exception e) {
            throw new KasperException(e.toString());
        }
    }

    public NodeReference createNode(String nodename) {
        try {
            PathParser parser = new PathParser();
            parser.addPathConventionally(nodename);
            var doc = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
            doc.createNode(parser.parsePath());
            networkPackage.put(doc.toString());
            return useNode(nodename);
        } catch (IOException e) {
            throw new KasperException(e.getMessage());
        }
    }

    public NodeReference useNode(String name){
        return new NodeReference(name, this);
    }



}