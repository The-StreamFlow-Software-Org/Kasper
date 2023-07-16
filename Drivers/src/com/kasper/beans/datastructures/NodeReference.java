package com.kasper.beans.datastructures;

import com.kasper.commons.aliases.CommandAlias;
import com.kasper.commons.authenticator.KasperCommons.Authenticator.PacketOuterClass;
import com.kasper.commons.authenticator.KasperCommons.Authenticator.PreparedPacket;
import com.kasper.commons.datastructures.KasperPathReference;
import com.kasper.commons.exceptions.ExperimentalFeatureException;
import com.kasper.commons.exceptions.KasperException;
import com.kasper.commons.Parser.PathParser;
import com.kasper.commons.Parser.TokenSender;

public class NodeReference extends AbstractReference{

    protected NodeReference(String nodeName, KasperBean serverInstance) {
        super("node");
        this.host = serverInstance.host;
        this.user = serverInstance.user;
        this.password = serverInstance.password;
        this.name = nodeName;
        this.kasperNitroWire = serverInstance.kasperNitroWire;
        this.threadID = serverInstance.threadID;
    }

    /**
     *
     * @param collectionName the name of the collection to generate a reference
     * @return an instance of the collection
     */
    public CollectionReference useCollection (String collectionName) {
        verifyConcurrency();
        try {
            PathParser parser = new PathParser();
            parser.addPath(collectionName);
            parser.addPath(name);
            kasperNitroWire.put(TokenSender.exist(parser.parsePath()).toByteArray());
            var bytes = kasperNitroWire.get();
            var packet = PacketOuterClass.Packet.parseFrom(bytes);
            TokenSender.resolveExceptions(packet);
        } catch (Exception e) {
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
        return new CollectionReference(collectionName, this);
    }

    /**
     *
     * @param path generate a reference with this node as a base. (i.e., nodeName.[args])
     * @return a KasperPathReference to the specified reference.
     */
    public KasperPathReference generatePathReference(String ... path) {
        verifyConcurrency();
        PathParser parser = new PathParser();
        for (var x : path) {
            parser.addPathConventionally(x);
        }
        parser.addPath(name);
        return new KasperPathReference(parser.parsePath());
    }

    /**
     *
     * @param collectionName of the collection to create
     * @return an instance of the created collection
     */
    public CollectionReference createCollection (String collectionName) {
        verifyConcurrency();
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

    /**
     * Deletes this node.
     */
    public void deleteThis () {
        verifyConcurrency();
        if (!EXPERIMENTAL_MODE) throw new ExperimentalFeatureException("deleteThis");
        try {
            PreparedPacket packet = new PreparedPacket();
            packet.setHeader(CommandAlias.DELETE);
            packet.addArg("path", generatePathReference(name).toStr());
            kasperNitroWire.put(packet.build().toByteArray());
            TokenSender.resolveExceptions(PacketOuterClass.Packet.parseFrom(kasperNitroWire.get()));
        } catch (Exception e){
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
    }


}
