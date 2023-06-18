package Kasper.BeansDriver.DataStructures;

import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperReference;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Exceptions.KasperIOException;
import KasperCommons.Network.NetworkPackage;
import KasperCommons.Parser.KasperConstructor;
import KasperCommons.Parser.KasperDocument;
import KasperCommons.Parser.KasperWriter;
import KasperCommons.Parser.PathParser;

import java.io.IOException;
import java.net.Socket;

public class KasperBean extends AbstractReference{



    public KasperReference generateRawReference (String ref){
        return new KasperReference(ref);
    }



    public KasperBean(String host, String user, String password) throws KasperException {
        this(host, user, password, 53182);
    }

    public KasperBean(String host, String user, String password, int port) throws KasperException {
        super("server");
        new KasperAccessAuthenticator("kasper.util.key");
        try {
            this.host = host;
            this.user = user;
            this.password = password;
            this.name = this.host;
            networkPackage = new NetworkPackage(new Socket(host, port));
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
            KasperConstructor.checkForExceptions(networkPackage.get());
            return useNode(nodename);
        } catch (IOException e) {
            throw new KasperException(e.getMessage());
        }
    }

    public NodeReference useNode(String name){
        try {
            var document = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
            PathParser parser = new PathParser();
            parser.addPath(name);
            document.doesExist(parser.parsePath());
            networkPackage.put(document.toString());
            var x = KasperDocument.constructor(networkPackage.get()).document.getElementsByTagName("args").item(0).getTextContent().equals("yes");
            if (!x) throw new KasperException("Kasper:> The node + '" + name + "' does not exist.");
        } catch (Exception e) {
            throw new KasperIOException(e.toString());
        }
        return new NodeReference(name, this);
    }



}