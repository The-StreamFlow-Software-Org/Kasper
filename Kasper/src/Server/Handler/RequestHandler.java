package Server.Handler;

import DataStructures.KasperCollection;
import DataStructures.KasperNode;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.Authenticator.KasperCommons.Authenticator.PacketOuterClass;
import KasperCommons.Authenticator.KasperCommons.Authenticator.PreparedPacket;
import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Exceptions.KasperIOException;
import KasperCommons.Exceptions.KasperObjectAlreadyExists;
import KasperCommons.Exceptions.NoSuchKasperObject;
import KasperCommons.Network.NetworkPackage;
import KasperCommons.Network.Timer;
import KasperCommons.Parser.KasperWriter;
import KasperCommons.Parser.PathParser;
import KasperCommons.Parser.TokenSender;
import Server.Parser.KasperConstructor;
import Server.SuperClass.KasperGlobalMap;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.LinkedList;


public class RequestHandler {
    protected NetworkPackage pack;
    public void handleQuery(PacketOuterClass.Packet packet, NetworkPackage pack) throws IOException {
        this.pack = pack;
        PreparedPacket packet1 = new PreparedPacket();
        packet1.setHeader(1);
        switch (packet.getHeader()) {
            case 3 -> createNode(packet);
            case 4 -> createCollection(packet);
            case 5 -> exists(packet);
        }

        /*switch (purpose) {
            case "set" -> setRequest(doc);
            case "get" -> getRequest(doc);
            case "create node" -> createNode(doc);
            case "create collection" -> createCollection(doc);
            case "has" -> has(doc);
            case "contains" -> contains(doc);
            default -> throw new NoSuchKasperObject("Reason:> Command '" + purpose + "' is not a recognized KasperDOM command.");

        } */


    }



    private void contains(Document document) throws IOException {
        var response = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
        try {
            var base_path = document.getElementsByTagName("path").item(0).getTextContent();
            var args = document.getElementsByTagName("args").item(0);
            var matchWith = args.getChildNodes().item(0).getTextContent();
            var list = PathCrawler.getAllThatHasPath(base_path, matchWith);
            response.response(list);
            pack.put(response.toString());
        } catch (Exception e) {
            response.raiseException(e);
            pack.put(response.toString());
        }

    }


    private void setRequest (Document document) throws IOException {
        if (true)return;
        /*
        var response = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
        try {
            var path = document.getElementsByTagName("path").item(0);
            var object = KasperConstructor.constructNode(document.getElementsByTagName("data").item(0).getChildNodes().item(0), path.getTextContent());
            var destination = KasperGlobalMap.findWithPath(path.getTextContent());
            var key = document.getElementsByTagName("data_key").item(0).getTextContent();

            if (destination instanceof KasperCollection c) {
                try {
                    c.getValue(key);
                    throw new KasperObjectAlreadyExists("Reason:> The object '" + key + "' already exists in path: " + path.getTextContent() + ".");
                } catch (NoSuchKasperObject ignored) {}
                c.put(key, object);
            }
            else if (destination instanceof KasperList l) {
                try {
                    if (key.equals("head")) {
                        object.path += ".0";
                        l.toList().push(object);
                    } else if (key.equals("tail")) {
                        object.path += "." + l.toList().size();
                        l.toList().add(object);
                    } else {
                        int index = Integer.parseInt(key);
                        var ll = (LinkedList)l.toList();
                        object.path += "." + index;
                        ll.add(index, object);
                    }
                } catch (Exception e) {
                    throw new KasperException("Reason:> Invalid list index was found. Use values [head/tail] to add an element to the head or tail respectively. Else, use a numeric string to specify the index.");
                }
            }
            else if (destination instanceof KasperMap m) {
                if (m.get(key) == null) {
                    PathParser parse = new PathParser();
                    parse.addPath(key);
                    m.put(key, object.setPath( object.path += ("." + parse.parsePath())));
                    m.put(key, object);
                }
                else
                    throw new KasperObjectAlreadyExists("Reason:> The object '" + key + "' already exists in path: " + path.getTextContent() + ".");
            } else {
                throw new KasperException("Reason:> Cannot conduct operation [set/append] to a string reference. Only doable in: [map/list]-type.");
            }
        } catch (Exception e) {
            response.raiseException(e);
            pack.put(response.toString());
            return;
        }
        response.sendOkResponse();
        pack.put(response.toString());

         */
    }


    public void getRequest(Document document) throws IOException {
        var response  = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
        Timer t = new Timer();
        t.start();
        try {
            try {
                var retrievedObject = KasperGlobalMap.findWithPath(document.getElementsByTagName("path").item(0).getTextContent());
                var doc = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
                doc.response(retrievedObject);
                pack.put(doc.toString());
            } catch (IOException e) {
                throw new KasperIOException(e.getMessage());
            }
        } catch (Exception e) {
            t.stop();
            response.raiseException(e);
            pack.put(response.toString());
        }
    }

    public void createNode (PacketOuterClass.Packet packet) throws IOException {
        try {
            var path = PathParser.unparsePath(packet.getArgsMap().get("name"));
            if (KasperGlobalMap.globalmap.get(path.get(0)) != null)
                throw new KasperObjectAlreadyExists("Reason:> The node '" + path.get(0) + "' already exists.");
            KasperGlobalMap.newNode(path.get(0));
        } catch (Exception e){
            var bufferProtocol = TokenSender.raise(TokenSender.classifyException(e), e.getMessage()).toByteArray();
            pack.put(bufferProtocol);
        }
        var bufferProtocol = TokenSender.responseOK().toByteArray();
        pack.put(bufferProtocol);
    }

    public void createCollection (PacketOuterClass.Packet packet) throws IOException {
        try {
            var path = PathParser.unparsePath(packet.getArgsMap().get("path"));
            var node = (KasperNode)KasperGlobalMap.getNode(path.get(0));
            boolean exists = false;
            try {
                node.useCollection(path.get(1));
                exists = true;
            } catch (Exception ignored) {}
            if (exists) throw new KasperObjectAlreadyExists("Reason:> The collection '" + path.get(1) + "' already exists.");
            try {
                KasperGlobalMap.getNode(path.get(0)).newCollection(path.get(1));
            } catch (Exception ignored){}
        } catch (Exception e){
            var bufferProtocol = TokenSender.raise(TokenSender.classifyException(e), e.getMessage()).toByteArray();
            pack.put(bufferProtocol);
        } var bufferProtocol = TokenSender.responseOK().toByteArray();
        pack.put(bufferProtocol);
    }

    public void exists(PacketOuterClass.Packet packet) throws IOException{
        PacketOuterClass.Packet prepared = null;
        try {
            PathParser parser = new PathParser();
            var unparsed = packet.getArgsMap().get("path");
            var path = PathParser.unparsePath(unparsed);
            for (var x : path) {
                parser.addPathConventionally(x);
            } var result = KasperGlobalMap.findWithPath(parser.parsePath());
            if (result == null) {
                prepared = TokenSender.raise(5, NoSuchKasperObject.buildCommand("object", path.get(path.size()-1), unparsed));
            } else prepared = TokenSender.responseOK();
            pack.put(prepared.toByteArray());
        } catch (Exception e) {
            prepared = TokenSender.raise(TokenSender.classifyException(e), e.getMessage());
            pack.put(prepared.toByteArray());
        }
    }
}
