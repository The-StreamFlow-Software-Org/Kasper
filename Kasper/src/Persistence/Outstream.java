package Persistence;

import datastructures.KasperCollection;
import datastructures.KasperNode;
import com.kasper.commons.authenticator.KasperAccessAuthenticator;
import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.Parser.KasperDocument;
import com.kasper.commons.Parser.KasperWriter;
import server.Parser.DiskIO;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Map;

@Deprecated
public class Outstream {
    private KasperDocument document;
    ArrayList<Node> nodeList = null;


    public Outstream (KasperNode n) {
        this.document = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
        document.addFor("reconstruction");
        nodeList = new ArrayList<>();
        serializeNode(n);

    }

    private void serializeNode (KasperNode node){
        var currNode = document.getTag("node");
        var key = document.createNode("node_key", node.getName());
        currNode.appendChild(key);
        var data = document.getTag("node_data");
        currNode.appendChild(data);
        for (var x : node.getData().entrySet()) {
            serializeCollection(x, data);
        } document.addArgs(currNode);
    }

    private void serializeCollection (Map.Entry<String, KasperObject> set, Node node) {
        var collection = (KasperCollection)set.getValue();
        var thisCollection = document.getTag("collection");
        var key = document.createNode("collection_key", collection.getName());
        var value = document.getTag("collection_data");
        for (var x : collection.getData().entrySet()){
            ;
            var entry_key = document.createNode("entry_key", x.getKey());
            value.appendChild(entry_key);
            value.appendChild(document.extract(x.getValue()));
        }
        thisCollection.appendChild(key);
        thisCollection.appendChild(value);
        node.appendChild(thisCollection);
    }

    public KasperDocument construct () {
        return document;
    }

    public Outstream chain (Outstream out){
        this.document.addQuery(out.construct().getQuery());
        return this;
    }

    public void bucketize () throws Exception {
        DiskIO.writeDocument(document);
    }



}
