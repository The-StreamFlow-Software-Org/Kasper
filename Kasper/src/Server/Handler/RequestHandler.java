package Server.Handler;

import DataStructures.KasperCollection;
import DataStructures.KasperNode;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Exceptions.KasperIOException;
import KasperCommons.Network.NetworkPackage;
import KasperCommons.Network.Operations;
import KasperCommons.Parser.KasperDocument;
import KasperCommons.Parser.KasperWriter;
import Persistence.Outstream;
import Server.Parser.KasperConstructor;
import Server.SuperClass.KasperGlobalMap;
import org.w3c.dom.Document;

import java.io.IOException;


public class RequestHandler {
    protected NetworkPackage pack;
    public void handleQuery(KasperDocument document, NetworkPackage pack){
        var doc = document.getDocument(KasperAccessAuthenticator.getKey());
        var purpose = doc.getElementsByTagName("for").item(0).getTextContent();
        this.pack = pack;

        if (purpose.equals("set")) {
            setRequest(doc);
        } else if (purpose.equals("get")) {
            getRequest(doc);
        }

    }

    private void setRequest (Document document)  {
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

    public void getRequest(Document document)  {
        try {
            var x = KasperGlobalMap.findWithPath(document.getElementsByTagName("path").item(0).getTextContent());
            var doc = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
            doc.response(x);
            pack.put(doc.toString());
        } catch (IOException e) {
            throw new KasperIOException(e.getMessage());
        }
    }
}
