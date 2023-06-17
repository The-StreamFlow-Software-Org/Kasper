package Server.Handler;

import DataStructures.KasperCollection;
import DataStructures.KasperNode;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Exceptions.KasperIOException;
import KasperCommons.Exceptions.NoSuchKasperObject;
import KasperCommons.Network.NetworkPackage;
import KasperCommons.Parser.KasperDocument;
import KasperCommons.Parser.KasperWriter;
import KasperCommons.Parser.PathParser;
import Server.Parser.KasperConstructor;
import Server.SuperClass.KasperGlobalMap;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.LinkedList;


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
        } else if (purpose.equals("has")) {
            has(doc);
        }

    }


    private void setRequest (Document document) throws IOException {
        var response = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
        try {
            var path = document.getElementsByTagName("path").item(0);
            var object = KasperConstructor.constructNode(document.getElementsByTagName("data").item(0).getChildNodes().item(0));
            var destination = KasperGlobalMap.findWithPath(path.getTextContent());
            var key = document.getElementsByTagName("data_key").item(0).getTextContent();

            if (destination instanceof KasperCollection c) {
                try {
                    c.getValue(key);
                    throw new KasperException("Reason:> The object '" + key + "' already exists in path: " + path.getTextContent() + ".");
                } catch (NoSuchKasperObject ignored) {}
                c.put(key, object);
            }
            else if (destination instanceof KasperList l) {
                try {
                    if (key.equals("head")) {
                        l.toList().push(object);
                    } else if (key.equals("tail")) {
                        l.toList().add(object);
                    } else {
                        int index = Integer.parseInt(key);
                        var ll = (LinkedList)l.toList();
                        ll.add(index, object);
                    }
                } catch (Exception e) {
                    throw new KasperException("Reason:> Invalid list index was found. Use values [head/tail] to add an element to the head or tail respectively. Else, use a numeric string to specify the index.");
                }
            }
            else if (destination instanceof KasperMap m) {
                if (m.get(key) == null) m.put(key, object);
                else
                    throw new KasperException("Reason:> The object '" + key + "' already exists in path: " + path.getTextContent() + ".");
            } else {
                throw new KasperException("Reason:> Cannot conduct operation [set/append] to a string reference. Only doable in: [map/list]-type.");
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
            if (KasperGlobalMap.globalmap.get(path.get(0)) != null) throw new KasperException("Reason:> The node '" + path.get(0) + "' already exists.");
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
            var node = (KasperNode)KasperGlobalMap.getNode(path.get(0));
            boolean exists = false;
            try {
                var collection = node.useCollection(path.get(1));
                exists = true;
            } catch (Exception ignored) {}
            if (exists) throw new KasperException("Reason:> The collection '" + path.get(1) + "' already exists.");
            try {
                KasperGlobalMap.getNode(path.get(0)).newCollection(path.get(1));
            } catch (Exception ignored){}
        } catch (Exception e){
            response.raiseException(e);
            pack.put(response.toString());
        } response.sendOkResponse();
        pack.put(response.toString());
    }

    public void has(Document document) throws IOException{
        var response  = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
        try {
            PathParser parser = new PathParser();
            var path = parser.unparsePath(document.getElementsByTagName("path").item(0).getTextContent());
            for (var x : path) {
                parser.addPathConventionally(x);
            } var result = KasperGlobalMap.findWithPath(parser.parsePath());
            if (result == null) {
                response.doesExistResponse(false);
            } else response.doesExistResponse(true);
            pack.put(response.toString());
        } catch (Exception e) {response.raiseException(e);
            pack.put(response.toString());
        }
    }
}
