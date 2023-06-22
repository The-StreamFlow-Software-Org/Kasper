package Kasper.BeansDriver.DataStructures;

import KasperCommons.Authenticator.KasperCommons.Authenticator.PacketOuterClass;
import KasperCommons.Authenticator.KasperCommons.Authenticator.PreparedPacket;
import KasperCommons.DataStructures.KasperReference;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Exceptions.KasperIOException;
import KasperCommons.Parser.PathParser;
import KasperCommons.Parser.TokenSender;

import java.io.IOException;

public class NodeReference extends AbstractReference{

    protected NodeReference(String nodeName, KasperBean serverInstance) {
        super("node");
        this.host = serverInstance.host;
        this.user = serverInstance.user;
        this.password = serverInstance.password;
        this.name = nodeName;
        this.networkPackage = serverInstance.networkPackage;
    }

    public CollectionReference useCollection (String referenceName) {
        try {
            PathParser parser = new PathParser();
            parser.addPath(referenceName);
            parser.addPath(name);
            networkPackage.put(TokenSender.exist(parser.parsePath()).toByteArray());
            var bytes = networkPackage.get();
            var packet = PacketOuterClass.Packet.parseFrom(bytes);
            TokenSender.resolveExceptions(packet);
        } catch (Exception e) {
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
        return new CollectionReference(referenceName, this);
    }

    public KasperReference generateReference (String ... path) {
        PathParser parser = new PathParser();
        for (var x : path) {
            parser.addPathConventionally(x);
        }
        parser.addPath(name);
        return new KasperReference(parser.parsePath());
    }

    public CollectionReference createCollection (String collectionName) {
        try {
            PathParser parser = new PathParser();
            parser.addPathConventionally(collectionName);
            parser.addPath(name);
            PreparedPacket packet = new PreparedPacket();
            packet.setHeader(4);
            packet.addArg("path", parser.parsePath());
            networkPackage.put(packet.build().toByteArray());
            TokenSender.resolveExceptions(PacketOuterClass.Packet.parseFrom(networkPackage.get()));
            return useCollection(collectionName);
        } catch (Exception e) {
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
    }


}
