package Kasper.BeansDriver.DataStructures;

import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperReference;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Parser.KasperWriter;
import KasperCommons.Parser.PathParser;

import java.io.IOException;

public class NodeReference extends AbstractReference{

    protected NodeReference(String nodeName, KasperBean serverInstance) {
        super("node");
        this.host = serverInstance.host;
        this.user = serverInstance.user;
        this.password = serverInstance.password;
        this.name = nodeName;
        this.networkPackage = serverInstance.networkPackage;
    }

    public CollectionReference useCollection (String referenceName) {
        return new CollectionReference(referenceName, this);
    }

    public KasperReference generateReference (String ... path) {
        PathParser parser = new PathParser();
        for (var x : path) {
            parser.addPathConventionally(x);
        }
        parser.addPath(name);
        return new KasperReference(parser.parsePath());
    }

    public CollectionReference createCollection (String collectionName) {
        try {
            PathParser parser = new PathParser();
            parser.addPathConventionally(collectionName);
            parser.addPath(name);
            var doc = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
            doc.createNode(parser.parsePath());
            networkPackage.put(doc.toString());
            return useCollection(collectionName);
        } catch (IOException e) {
            throw new KasperException(e.getMessage());
        }
    }


}
