package Persistence;

import DataStructures.KasperNode;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.Exceptions.InvalidPersistenceData;
import KasperCommons.Parser.DiskIO;
import KasperCommons.Parser.KasperDocument;
import Server.SuperClass.KasperGlobalMap;

public class InstantiatorService {

    public static void start() throws Exception {
        var data = KasperDocument.constructor(DiskIO.documentStringRetrieval());
        var document = data.getDocument(KasperAccessAuthenticator.getKey());
        var query  = document.getElementsByTagName("query").item(0);
        var queryChild = query.getChildNodes();
        var purpose = queryChild.item(0);
        if (!purpose.getTextContent().equals("reconstruction")) throw new InvalidPersistenceData("The persistence data has an invalid header for args, provided: '" + purpose.getTextContent() + "' instead of 'reconstruction'");

        var args = queryChild.item(1);
        var nodes = args.getChildNodes();
        for (int i = 0; i<nodes.getLength(); i++){
            var node = nodes.item(i);
            var attrib = node.getChildNodes();
            KasperGlobalMap.getNodes().put(attrib.item(0).getTextContent(), new KasperNode(node));
        }
        data.clear();
    }

    public static void close () throws Exception {
        var x = KasperGlobalMap.getNodes();
        var iteratorset = x.entrySet();
        Outstream root = null;
        for (var i : iteratorset){
            if (root == null) {
                root = new Outstream(i.getValue());
            } else {
                root.chain(new Outstream(i.getValue()));
            }
        }
        root.bucketize();
    }
}
