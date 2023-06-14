package DataStructures;

import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Exceptions.KasperNodeNotFound;
import KasperCommons.Parser.KasperDocument;
import org.w3c.dom.Node;

import java.util.HashMap;

public class KasperNode extends KasperServerAbstracts {

    public KasperNode(String name) {
        super("node");
        this.name = name;
    }

    public KasperNode (Node nodeConstructor) {
        super("node");
        var constChild = nodeConstructor.getChildNodes();
        var collections = constChild.item(1).getChildNodes();
        this.name = constChild.item(0).getTextContent();
        for (int i=0; i<collections.getLength(); i++){
            addCollection(new KasperCollection(collections.item(i), this));
        }
    }

    public KasperNode addCollection (KasperCollection value){
        data.put(value.getName(), value);
        return this;
    }

    public KasperCollection getCollection (String name){
        return (KasperCollection) data.get(name);
    }







}
