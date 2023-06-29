package Kasper.BeansDriver.DataStructures;

import Boost.JSONCache;
import Kasper.BeansDriver.Network.SocketHolder;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.Authenticator.KasperCommons.Authenticator.PacketOuterClass;
import KasperCommons.Authenticator.KasperCommons.Authenticator.PreparedPacket;
import KasperCommons.DataStructures.KasperReference;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Exceptions.KasperIOException;
import KasperCommons.Network.KasperNitroWire;
import KasperCommons.Parser.PathParser;
import KasperCommons.Parser.TokenSender;

import java.io.IOException;
import java.net.Socket;

public class KasperBean extends AbstractReference{

    protected SocketHolder socketHolder;



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
            kasperNitroWire = new KasperNitroWire(new Socket(host, port), true);
            JSONCache.init();
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
            kasperNitroWire.put(packet.build().toByteArray());
            TokenSender.resolveExceptions(PacketOuterClass.Packet.parseFrom(kasperNitroWire.get()));
            return useNode(nodename);
        } catch (Exception e) {
           throw new KasperException(e.getMessage());
        }
    }

    public NodeReference useNode(String name){
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