package Kasper.BeansDriver.DataStructures;

import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.DataStructures.KasperReference;
import KasperCommons.Exceptions.KasperException;
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
        host = parent.host;
        user = parent.host;
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
            throw new KasperIOException(e.toString());
        }
    }

    public KasperObject getKey(KasperReference reference){
        try {
            var document = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
            document.getRequest(reference.toStr());
            networkPackage.put(document.toString());
            return new KasperConstructor(KasperDocument.constructor(networkPackage.get())).constructObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new KasperIOException(e.toString());
        }
    }

    public void setKey(KasperReference referencePath, String key, KasperObject value) throws KasperException {
        try {
            var document = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
            document.setRequest(referencePath.toStr(), key, value);
            networkPackage.put(document.toString());
            KasperConstructor.checkForExceptions(networkPackage.get());
        } catch (Exception e){
            throw new KasperException(e.getMessage());
        }
    }

    public void setKey (KasperReference referencePath, String key, String value) throws KasperException{
        setKey(referencePath, key, KasperObject.str(value));
    }

    public void setKey (String key, String value) throws KasperException {
        setKey(key, KasperObject.str(value));
    }

    public void setKey(String key, KasperObject value) {
        setKey(generateReference(), key, value);
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

    public KasperReference rawReference (String rawPath) {
        PathParser parser = new PathParser();
        var path = parser.unparsePath(rawPath);
        for (var x : path) {
            parser.addPathConventionally(x);
        }
        parser.addPath(name);
        parser.addPath(parent.name);
        return new KasperReference(parser.parsePath());
    }







}
