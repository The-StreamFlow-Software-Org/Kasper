package com.kasper.beans.datastructures;

import com.kasper.Boost.JSONCache;
import com.kasper.commons.authenticator.KasperCommons.Authenticator.PacketOuterClass;
import com.kasper.commons.authenticator.KasperCommons.Authenticator.PreparedPacket;
import com.kasper.commons.datastructures.KasperPathReference;
import com.kasper.commons.exceptions.KasperException;
import com.kasper.commons.exceptions.KasperIOException;
import com.kasper.commons.Network.KasperNitroWire;
import com.kasper.commons.Parser.PathParser;
import com.kasper.commons.Parser.TokenSender;

import java.io.IOException;
import java.net.Socket;

public class KasperBean extends AbstractReference{




    public KasperPathReference generateRawPathReference(String ref){
        return new KasperPathReference(ref);
    }





    public KasperBean(String host, String user, String password) throws KasperException {
        this(host, user, password, 53182);
    }

    public KasperBean(String host, String user, String password, int port) throws KasperException {
        super("server");
        threadID = Thread.currentThread().getId();
        try {
            this.host = host;
            this.user = user;
            this.password = password;
            this.name = this.host;
            kasperNitroWire = new KasperNitroWire(new Socket(host, port));
            JSONCache.init();
        } catch (IOException e) {
            throw new KasperIOException("thrown by KasperDriver:> Make sure that you are connected to the KasperEngine instance.");
        }
    }



    public NodeReference createNode(String nodename) {
        verifyConcurrency();
        try {
            PathParser parser = new PathParser();
            parser.addPathConventionally(nodename);
            PreparedPacket packet = new PreparedPacket();
            packet.setHeader(3);
            packet.addArg("name", parser.parsePath());
            kasperNitroWire.put(packet.build().toByteArray());
            TokenSender.resolveExceptions(PacketOuterClass.Packet.parseFrom(kasperNitroWire.get()));
            return useNode(nodename);
        } catch (Exception e) {
           throw new KasperException(e.getMessage());
        }
    }

    public NodeReference useNode(String name){
        verifyConcurrency();
        try {
            PathParser parser = new PathParser();
            parser.addPath(name);
            kasperNitroWire.put(TokenSender.exist(parser.parsePath()).toByteArray());
            var bytes = kasperNitroWire.get();
            var packet = PacketOuterClass.Packet.parseFrom(bytes);
            TokenSender.resolveExceptions(packet);
        } catch (Exception e) {
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
        return new NodeReference(name, this);
    }



}