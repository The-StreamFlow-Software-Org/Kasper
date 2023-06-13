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

    public KasperNode (Node nodeConstructor) throws KasperNodeNotFound {
        super("node");



        if (thisNode == null) {
            throw new KasperNodeNotFound("clientExcept: Node of name " + name + " not found in the database.");
        }

        // construct from here
    }

    public KasperNode addCollection (String key, KasperCollection value){
        data.put(key, value);
        return this;
    }







}
