package DataStructures;

import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Parser.KasperConstructor;
import org.w3c.dom.Node;

public class KasperCollection extends KasperServerAbstracts {



    public KasperCollection(KasperNode parent, String name) {
        super("collection");
        this.name = name;
        this.parent = parent;
    }

    public KasperCollection (Node thisNode, KasperNode parent){
        super("collection");
        this.thisNode = thisNode;
        this.parent = parent;
        var meta = thisNode.getChildNodes();
        this.name = meta.item(0).getTextContent();
        var data = thisNode.getChildNodes().item(1);
        var entries = data.getChildNodes();
        for (int i=0; i<entries.getLength(); i+=2){
            var key = entries.item(i).getTextContent();
            var constructor = KasperConstructor.constructNode(entries.item(i+1));
            getData().put(key, constructor);
        }
    }

    public KasperCollection addData (String key, KasperObject value) {
        data.put(key, value);
        return this;
    }

    public KasperObject getValue (String key) {
        return data.get(key);
    }

}
