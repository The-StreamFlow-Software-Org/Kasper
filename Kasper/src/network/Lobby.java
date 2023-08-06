package network;

import com.kasper.commons.authenticator.Meta;
import com.kasper.commons.Network.Timer;
import Persistence.InstantiatorService;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Lobby {
    private final ServerSocket server;
    private final ServerSocket nitroServer;
    public static boolean ending = false;

    private static Lobby instance;

    public static void close () throws IOException {
        instance.server.close();
        instance.nitroServer.close();

    }




    public void enqueueClients()  {
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


     public Lobby () throws IOException {
        server = new ServerSocket(Meta.port);
        nitroServer = new ServerSocket(Meta.port+1);
        String msg = "Kasper:> Current memory usage of all stored data: ";
        System.out.println("Kasper:> Now ready to accept connections in port: " + Meta.port + ".");
        System.out.println("Kasper:> Device host: " + InetAddress.getLocalHost().getHostAddress());
    }

    public Lobby (ServerSocket socket, ServerSocket nitro) throws IOException {
        this.server = socket;
        this.nitroServer = nitro;
    }
}
