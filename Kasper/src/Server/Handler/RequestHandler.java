package Server.Handler;

import DataStructures.KasperCollection;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.Exceptions.KasperIOException;
import KasperCommons.Network.NetworkPackage;
import KasperCommons.Parser.KasperDocument;
import KasperCommons.Parser.KasperWriter;
import KasperCommons.Parser.PathParser;
import Server.Parser.KasperConstructor;
import Server.SuperClass.KasperGlobalMap;
import org.w3c.dom.Document;

import java.io.IOException;


public class RequestHandler {
    protected NetworkPackage pack;
    public void handleQuery(KasperDocument document, NetworkPackage pack) throws IOException {
        var doc = document.getDocument(KasperAccessAuthenticator.getKey());
        var purpose = doc.getElementsByTagName("for").item(0).getTextContent();
        this.pack = pack;

        if (purpose.equals("set")) {
            setRequest(doc);
        } else if (purpose.equals("get")) {
            getRequest(doc);
        } else if (purpose.equals("create node")) {
            createNode(doc);
        } else if (purpose.equals("create collection")){
            createCollection(doc);
        }

    }

    private void setRequest (Document document) throws IOException {
        var response  = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
        try {
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
        } catch (Exception e) {
            response.raiseException(e);
            pack.put(response.toString());
        }
        response.sendOkResponse();
        pack.put(response.toString());
    }

    public void getRequest(Document document) throws IOException {
        var response  = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
        try {
            try {
                var x = KasperGlobalMap.findWithPath(document.getElementsByTagName("path").item(0).getTextContent());
                var doc = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
                doc.response(x);
                pack.put(doc.toString());
            } catch (IOException e) {
                throw new KasperIOException(e.getMessage());
            }
        } catch (Exception e) {
            response.raiseException(e);
            pack.put(response.toString());
        }
    }

    public void createNode (Document document) throws IOException {
        var response  = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
        try {
            PathParser parser = new PathParser();
            var path = parser.unparsePath(document.getElementsByTagName("path").item(0).getTextContent());
            KasperGlobalMap.newNode(path.get(0));
        } catch (Exception e){
            response.raiseException(e);
            pack.put(response.toString());
        } response.sendOkResponse();
        pack.put(response.toString());
    }

    public void createCollection (Document document) throws IOException {
        var response  = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
        try {
        PathParser parser = new PathParser();
        var path = parser.unparsePath(document.getElementsByTagName("path").item(0).getTextContent());
        KasperGlobalMap.getNode(path.get(0)).newCollection(path.get(1));
        } catch (Exception e){
            response.raiseException(e);
            pack.put(response.toString());
        } response.sendOkResponse();
        pack.put(response.toString());
    }
}
