package Kasper.BeansDriver.DataStructures;

import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperReference;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Exceptions.KasperIOException;
import KasperCommons.Parser.KasperConstructor;
import KasperCommons.Parser.KasperDocument;
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
        try {
            var document = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
            PathParser parser = new PathParser();
            parser.addPath(referenceName);
            parser.addPath(name);
            document.doesExist(parser.parsePath());
            networkPackage.put(document.toString());
            var x = KasperDocument.constructor(networkPackage.get()).document.getElementsByTagName("args").item(0).getTextContent().equals("yes");
            if (!x) throw new KasperException("Kasper:> The collection + '" + referenceName + "' does not exist.");
        } catch (Exception e) {
            throw new KasperIOException(e.toString());
        }
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
            doc.createCollection(parser.parsePath());
            networkPackage.put(doc.toString());
            KasperConstructor.checkForExceptions(networkPackage.get());
            return useCollection(collectionName);
        } catch (IOException e) {
            throw new KasperException(e.getMessage());
        }
    }


}
