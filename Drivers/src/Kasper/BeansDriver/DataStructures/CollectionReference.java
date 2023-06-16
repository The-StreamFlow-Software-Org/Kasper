package Kasper.BeansDriver.DataStructures;

import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.DataStructures.KasperReference;
import KasperCommons.Exceptions.KasperIOException;
import KasperCommons.Parser.KasperConstructor;
import KasperCommons.Parser.KasperDocument;
import KasperCommons.Parser.KasperWriter;
import KasperCommons.Parser.PathParser;

public class CollectionReference extends AbstractReference{
    protected NodeReference parent;
    protected String nodeName;

    protected CollectionReference(String name, NodeReference parent) {
        super("collection");
        this.parent = parent;
        password = parent.password;
        serverLocation = parent.serverLocation;
        user = parent.serverLocation;
        nodeName = parent.name;
        this.name = name;
        this.networkPackage = parent.networkPackage;
    }

    public KasperObject getKey(String keyName){
        try {
            var document = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
            PathParser parser = new PathParser();
            parser.addPath(keyName);
            parser.addPath(name);
            parser.addPath(parent.name);
            document.getRequest(parser.parsePath());
            networkPackage.put(document.toString());
            return new KasperConstructor(KasperDocument.constructor(networkPackage.get())).constructObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new KasperIOException(e.toString());
        }
    }

    public KasperReference generateReference (String ... path) {
        PathParser parser = new PathParser();
        for (var x : path) {
            parser.addPathConventionally(x);
        }
        parser.addPath(name);
        parser.addPath(parent.name);
        return new KasperReference(parser.parsePath());
    }






}
