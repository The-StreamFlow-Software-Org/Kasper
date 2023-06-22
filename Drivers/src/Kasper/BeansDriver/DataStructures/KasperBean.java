package Kasper.BeansDriver.DataStructures;

import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.Authenticator.KasperCommons.Authenticator.PacketOuterClass;
import KasperCommons.Authenticator.KasperCommons.Authenticator.PreparedPacket;
import KasperCommons.DataStructures.KasperReference;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Exceptions.KasperIOException;
import KasperCommons.Network.NetworkPackage;
import KasperCommons.Parser.PathParser;
import KasperCommons.Parser.TokenSender;

import java.io.IOException;
import java.net.Socket;

public class KasperBean extends AbstractReference{



    public KasperReference generateRawReference (String ref){
        return new KasperReference(ref);
    }





    public KasperBean(String host, String user, String password) throws KasperException {
        this(host, user, password, 53182);
    }

    public KasperBean(String host, String user, String password, int port) throws KasperException {
        super("server");
        new KasperAccessAuthenticator("kasper.util.key");
        try {
            this.host = host;
            this.user = user;
            this.password = password;
            this.name = this.host;
            networkPackage = new NetworkPackage(new Socket(host, port));
        } catch (IOException e) {
            throw new KasperIOException("thrown by KasperDriver:> Make sure that you are connected to the KasperEngine instance.");
        }
    }



    public NodeReference createNode(String nodename) {
        try {
            PathParser parser = new PathParser();
            parser.addPathConventionally(nodename);
            PreparedPacket packet = new PreparedPacket();
            packet.setHeader(3);
            packet.addArg("name", parser.parsePath());
            networkPackage.put(packet.build().toByteArray());
            TokenSender.resolveExceptions(PacketOuterClass.Packet.parseFrom(networkPackage.get()));
            return useNode(nodename);
        } catch (Exception e) {
           throw new KasperException(e.getMessage());
        }
    }

    public NodeReference useNode(String name){
        try {
            PathParser parser = new PathParser();
            parser.addPath(name);
            networkPackage.put(TokenSender.exist(parser.parsePath()).toByteArray());
            var bytes = networkPackage.get();
            var packet = PacketOuterClass.Packet.parseFrom(bytes);
            TokenSender.resolveExceptions(packet);
        } catch (Exception e) {
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
        return new NodeReference(name, this);
    }



}