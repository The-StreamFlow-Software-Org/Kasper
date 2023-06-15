package Kasper.BeansDriver.DataStructures;

import Kasper.BeansDriver.Listeners.QueryListener;
import KasperCommons.DataStructures.KasperReference;
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
    }

    public QueryListener getKey(String keyName){
        return new QueryListener(null);
    }

    public KasperReference generateReference (String ... args) {
        PathParser parser = new PathParser();
        for (var x : args) {
            parser.addPathConventionally(x);
        }
        parser.addPath(name);
        parser.addPath(parent.name);
        return new KasperReference(parser.parsePath());
    }






}
