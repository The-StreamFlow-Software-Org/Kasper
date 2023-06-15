package Server.Handler;

import DataStructures.KasperCollection;
import DataStructures.KasperNode;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.Parser.KasperDocument;
import Server.Parser.KasperConstructor;
import Server.SuperClass.KasperGlobalMap;
import org.w3c.dom.Document;


public class RequestHandler {

    public static void handleQuery(KasperDocument document){
        var doc = document.getDocument(KasperAccessAuthenticator.getKey());
        var purpose = doc.getElementsByTagName("for").item(0).getTextContent();


        if (purpose.equals("set")) {
            setRequest(doc);
        }

    }

    private static void setRequest (Document document)  {
        var object = KasperConstructor.constructNode(document.getElementsByTagName("data").item(0).getChildNodes().item(0));
        var destination = KasperGlobalMap.findWithPath(document.getElementsByTagName("path").item(0).getTextContent());
        var key = document.getElementsByTagName("data_key").item(0).getTextContent();
        try {
            var collection = (KasperCollection) destination;
            collection.put(key, object);
        } catch (ClassCastException e) {
            try {
                object.toMap().put(key, object);
            } catch (ClassCastException r) {
                object.toList().add(object);
            }
        }
    }
}
