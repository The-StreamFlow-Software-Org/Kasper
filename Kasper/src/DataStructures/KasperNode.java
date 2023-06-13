package DataStructures;

import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Exceptions.KasperNodeNotFound;
import KasperCommons.Parser.KasperDocument;
import org.w3c.dom.Node;

public class KasperNode extends KasperServerAbstracts {

    public KasperNode(String name) {
        super("node");
        this.name = name;
    }

    public KasperNode (KasperDocument construct, String name) throws KasperNodeNotFound {
        super("node");
        this.name = name;
        var nodes = construct.getDocument(KasperAccessAuthenticator.getKey()).getElementsByTagName("args").item(0).getChildNodes();
        thisNode = null;

        // finds the node from the node collection
        for (int i=0; i<nodes.getLength(); i++){
            if (nodes.item(i).getTextContent().equals(name)) thisNode = nodes.item(i);
        }


        if (thisNode == null) {
            throw new KasperNodeNotFound("clientExcept: Node of name " + name + " not found in the database.");
        }

        // construct from here
    }





}
