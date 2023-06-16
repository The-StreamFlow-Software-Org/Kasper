package Network;

import KasperCommons.Network.NetworkPackage;
import Server.SuperClass.Meta;

import java.io.IOException;
import java.net.ServerSocket;

public class Lobby {
    private ServerSocket server;

    private static Lobby instance;

    public static void acceptConnections() throws IOException {
        init();
        System.out.println("Kasper:> Ready to accept connections.");
        while (true) {
            new Room(new NetworkPackage(instance.server.accept()));
        }
    }
    private static void init () throws IOException {
    if (instance == null) instance = new Lobby();
    }

    private Lobby () throws IOException {
        server = new ServerSocket(Meta.port);
        System.out.println("Kasper:> Now ready to accept connections in port: " + Meta.port + ".");
    }
}
