package DataStructures;

import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Exceptions.KasperException;
import org.w3c.dom.Node;

public class KasperCollection extends KasperServerAbstracts {



    public KasperCollection(KasperNode parent, String name) {
        super("collection");
        this.name = name;
        this.parent = parent;
    }

    public KasperCollection (Node thisNode, KasperNode parent, String name){
        super("collection");
        this.thisNode = thisNode;
        this.name = name;
        this.parent = parent;
    }

}
