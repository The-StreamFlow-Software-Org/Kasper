package Server.Handler;

import Computations.DeleteResolver;
import DataStructures.KasperCollection;
import DataStructures.KasperNode;
import DataStructures.KasperServerAbstracts;
import KasperCommons.Aliases.CommandAlias;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.Authenticator.KasperCommons.Authenticator.PacketOuterClass;
import KasperCommons.Authenticator.KasperCommons.Authenticator.PreparedPacket;
import KasperCommons.DataStructures.*;
import KasperCommons.Exceptions.InvalidUpdateQuery;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Exceptions.KasperObjectAlreadyExists;
import KasperCommons.Exceptions.NoSuchKasperObject;
import KasperCommons.Network.KasperNitroWire;
import KasperCommons.Parser.KasperWriter;
import KasperCommons.Parser.PathParser;
import KasperCommons.Parser.TokenSender;
import Persistence.Cache;
import Server.SuperClass.KasperGlobalMap;
import org.w3c.dom.Document;

import java.io.IOException;


public class RequestHandler {
    protected KasperNitroWire pack;

    public void handleQuery(PacketOuterClass.Packet packet, KasperNitroWire pack) throws IOException {
        this.pack = pack;
        PreparedPacket packet1 = new PreparedPacket();
        switch (packet.getHeader()) {
            case CommandAlias.SET -> setRequest(packet);
            case CommandAlias.GET -> getRequest(packet);
            case CommandAlias.CREATE_NODE -> createNode(packet);
            case CommandAlias.CREATE_COLLECTION-> createCollection(packet);
            case CommandAlias.EXISTS -> exists(packet);
            case CommandAlias.CLEAR -> clear();
            case CommandAlias.UPDATE -> update(packet);
            case CommandAlias.DELETE -> delete(packet);

            default -> {
                pack.put(TokenSender.raise(2, "KasperEngine:> Driver failure, unknown command sent.").toByteArray());
            }
        }

    }



    private void delete (PacketOuterClass.Packet packet) throws IOException {
        try {
            var objToDel = KasperGlobalMap.findWithPath(packet.getArgsMap().get("path"));
            DeleteResolver.delete(objToDel);
            Cache.invalidateObject(objToDel);
            pack.put(TokenSender.responseOK().toByteArray());
        } catch (Exception e) {
            pack.put(TokenSender.raise(
                            TokenSender.classifyException(e), e.getMessage())
                    .toByteArray());
        }
    }


    private void update (PacketOuterClass.Packet packet) throws IOException {
        try {
            var path = packet.getArgsMap().get("path");
            var updateThis = KasperGlobalMap.findWithPath(path);
            if (updateThis instanceof KasperServerAbstracts) {
                throw new InvalidUpdateQuery(path);
            }
            var newObj = JSONUtils.parseJson(packet.getData());
            ProtectedUtils.updateTo(updateThis, newObj);
            Cache.set(path, newObj);
            pack.put(TokenSender.responseOK().toByteArray());
        } catch (Exception e) {
            pack.put(TokenSender.raise(
                            TokenSender.classifyException(e), e.getMessage())
                    .toByteArray());
        }
    }

    private void clear() throws IOException {
        KasperGlobalMap.globalmap.clear();
        Cache.cache().invalidateAll();
        pack.put(TokenSender.responseOK().toByteArray());
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


    private void setRequest(PacketOuterClass.Packet packet) throws IOException {
        try {
            var path = packet.getArgsMap().get("path");
            var object = JSONUtils.parseJson(packet.getData());
            var key = packet.getArgsMap().get("key");
            var destination = KasperGlobalMap.findWithPath(path);
            ProtectedUtils.setParent(object, destination);
            if (destination instanceof KasperCollection c) {
                try {
                    c.getValue(key);
                    throw new KasperObjectAlreadyExists("Reason:> The object '" + key + "' already exists in path: " + path + ".");
                } catch (NoSuchKasperObject ignored) {
                }
                c.put(key, object);
            } else if (destination instanceof KasperList l) {
                try {
                    if (key.equals("head")) {
                        l.toList().push(object);
                    } else if (key.equals("tail")) {
                        l.toList().add(object);
                    } else {
                        int index = Integer.parseInt(key);
                        var ll = (LockedLL) l.toList();
                        ll.add(index, object);
                    }
                } catch (Exception e) {
                    throw new KasperException("Reason:> Invalid list index was found. Use values [head/tail] to add an element to the head or tail respectively. Else, use a numeric string to specify the index.");
                }
            } else if (destination instanceof KasperMap m) {
                if (m.get(key) == null) {
                    m.put(key, object);
                } else
                    throw new KasperObjectAlreadyExists("Reason:> The object '" + key + "' already exists in path: " + path + ".");
            } else {
                throw new KasperException("Reason:> Cannot conduct operation [set/append] to a string reference. Only doable in: [map/list]-type.");
            }
        } catch (Exception e) {
            pack.put(TokenSender.raise(
                            TokenSender.classifyException(e), e.getMessage())
                    .toByteArray());
            return;
        }
        pack.put(
                TokenSender.responseOK()
                        .toByteArray());
    }


    public void getRequest(PacketOuterClass.Packet packet) throws IOException {
        var path = packet.getArgsMap().get("path");
        try {
            var retrievedObject = KasperGlobalMap.findWithPath(path);
            pack.put(
                    TokenSender.sendObjectResponse(retrievedObject, path)
                            .toByteArray());
        } catch (Exception e) {
            pack.put(
                    TokenSender.raise(TokenSender.classifyException(e), e.getMessage())
                            .toByteArray());
        }
    }

    public void createNode(PacketOuterClass.Packet packet) throws IOException {
        try {
            var path = PathParser.unparsePath(packet.getArgsMap().get("name"));
            if (KasperGlobalMap.globalmap.get(path.get(0)) != null)
                throw new KasperObjectAlreadyExists("Reason:> The node '" + path.get(0) + "' already exists.");
            KasperGlobalMap.newNode(path.get(0));
        } catch (Exception e) {
            var bufferProtocol = TokenSender.raise(TokenSender.classifyException(e), e.getMessage()).toByteArray();
            pack.put(bufferProtocol);
        }
        var bufferProtocol = TokenSender.responseOK().toByteArray();
        pack.put(bufferProtocol);
    }

    public void createCollection(PacketOuterClass.Packet packet) throws IOException {
        try {
            var path = PathParser.unparsePath(packet.getArgsMap().get("path"));
            var node = (KasperNode) KasperGlobalMap.getNode(path.get(0));
            boolean exists = false;
            try {
                node.useCollection(path.get(1));
                exists = true;
            } catch (Exception ignored) {
            }
            if (exists)
                throw new KasperObjectAlreadyExists("Reason:> The collection '" + path.get(1) + "' already exists.");
            try {
                KasperGlobalMap.getNode(path.get(0)).newCollection(path.get(1));
            } catch (Exception ignored) {
            }
        } catch (Exception e) {
            var bufferProtocol = TokenSender.raise(TokenSender.classifyException(e), e.getMessage()).toByteArray();
            pack.put(bufferProtocol);
        }
        var bufferProtocol = TokenSender.responseOK().toByteArray();
        pack.put(bufferProtocol);
    }

    public void exists(PacketOuterClass.Packet packet) throws IOException {
        PacketOuterClass.Packet prepared = null;
        try {
            PathParser parser = new PathParser();
            var unparsed = packet.getArgsMap().get("path");
            var path = PathParser.unparsePath(unparsed);
            for (var x : path) {
                parser.addPathConventionally(x);
            }
            var result = KasperGlobalMap.findWithPath(parser.parsePath());
            if (result == null) {
                prepared = TokenSender.raise(5, NoSuchKasperObject.buildCommand("object", path.get(path.size() - 1), unparsed));
            } else prepared = TokenSender.responseOK();
            pack.put(prepared.toByteArray());
        } catch (Exception e) {
            prepared = TokenSender.raise(TokenSender.classifyException(e), e.getMessage());
            pack.put(prepared.toByteArray());
        }
    }
}
