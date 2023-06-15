package Kasper.BeansDriver.DataStructures;

import KasperCommons.DataStructures.KasperReference;
import KasperCommons.Parser.PathParser;

public class NodeReference extends AbstractReference{

    protected NodeReference(String nodeName, KasperBean serverInstance) {
        super("node");
        this.serverLocation = serverInstance.serverLocation;
        this.user = serverInstance.user;
        this.password = serverInstance.password;
        this.name = nodeName;
    }

    public CollectionReference useCollection (String referenceName) {
        return new CollectionReference(referenceName, this);
    }

    public KasperReference generateReference (String ... args) {
        PathParser parser = new PathParser();
        for (var x : args) {
            parser.addPathConventionally(x);
        }
        parser.addPath(name);
        return new KasperReference(parser.parsePath());
    }
}
