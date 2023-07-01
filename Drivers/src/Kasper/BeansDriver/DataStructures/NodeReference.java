package Kasper.BeansDriver.DataStructures;

import KasperCommons.Authenticator.KasperCommons.Authenticator.PacketOuterClass;
import KasperCommons.Authenticator.KasperCommons.Authenticator.PreparedPacket;
import KasperCommons.DataStructures.KasperPathReference;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Parser.PathParser;
import KasperCommons.Parser.TokenSender;

public class NodeReference extends AbstractReference{

    protected NodeReference(String nodeName, KasperBean serverInstance) {
        super("node");
        this.host = serverInstance.host;
        this.user = serverInstance.user;
        this.password = serverInstance.password;
        this.name = nodeName;
        this.kasperNitroWire = serverInstance.kasperNitroWire;
    }

    public CollectionReference useCollection (String referenceName) {
        try {
            PathParser parser = new PathParser();
            parser.addPath(referenceName);
            parser.addPath(name);
            kasperNitroWire.put(TokenSender.exist(parser.parsePath()).toByteArray());
            var bytes = kasperNitroWire.get();
            var packet = PacketOuterClass.Packet.parseFrom(bytes);
            TokenSender.resolveExceptions(packet);
        } catch (Exception e) {
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
        return new CollectionReference(referenceName, this);
    }

    public KasperPathReference generatePathReference(String ... path) {
        PathParser parser = new PathParser();
        for (var x : path) {
            parser.addPathConventionally(x);
        }
        parser.addPath(name);
        return new KasperPathReference(parser.parsePath());
    }

    public CollectionReference createCollection (String collectionName) {
        try {
            PathParser parser = new PathParser();
            parser.addPathConventionally(collectionName);
            parser.addPath(name);
            PreparedPacket packet = new PreparedPacket();
            packet.setHeader(4);
            packet.addArg("path", parser.parsePath());
            kasperNitroWire.put(packet.build().toByteArray());
            TokenSender.resolveExceptions(PacketOuterClass.Packet.parseFrom(kasperNitroWire.get()));
            return useCollection(collectionName);
        } catch (Exception e) {
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
    }


}
