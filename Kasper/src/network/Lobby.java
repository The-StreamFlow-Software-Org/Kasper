package network;

import com.kasper.commons.authenticator.Meta;
import com.kasper.commons.Network.KasperNitroWire;
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

    public static void acceptConnections() throws Exception {
        init();
        System.out.println("Kasper:> Ready to accept connections.");


        Thread t = new Thread(()->{
            while (!ending) {
                try {
                    Thread.sleep(Meta.snapshotTimeout);
                    System.gc();
                    System.out.println("Kasper:> Writing snapshot to disk" + "...");
                    InstantiatorService.writeBackup();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Pool.newThread(t);

        final boolean[] doubleSIGINT = {false};


        Signal.handle(new Signal("INT"), (Signal sig)-> {
                try {
                    if (doubleSIGINT[0]) {
                        System.exit(0);
                    } doubleSIGINT[0] = true;
                    ending = true;
                    System.out.println("Kasper:> Instantiating the closing service. To force close the server, use 'ctrl + c' again. Warning: This may cause data loss.");
                    InstantiatorService.close();
                    System.out.println("Kasper:> Data snapshots saved after " + Timer.getTimer().stop() + "s.");
                    System.out.println("Kasper says bye! :)");
                    System.exit(0);
                } catch (Exception e) {
                    System.out.println("Kasper:> An exception occurred when saving the data snapshots. Please check the backups.");
                    System.exit(0);
                }
                System.exit(0);
        });

        Signal.handle(new Signal("TERM"), new SignalHandler() {
            public void handle(Signal signal) {
                try {
                    ending = true;
                    Timer.getTimer().start();
                    InstantiatorService.close();
                    System.out.println("Kasper:> Data snapshots saved after " + Timer.getTimer().stop() + "s.");
                    System.out.println("Kasper:> Use 'ctrl + c' to invoke termination.");
                } catch (Exception e) {
                    System.out.println("Kasper:> An exception occurred when saving the data snapshots. Please check the backups.");
                    System.exit(0);
                }
                System.exit(0);
            }
        });
        while (true) {
            var initWire = (new KasperNitroWire(instance.server.accept()));
            new Room(initWire);
        }
    }
    private static void init () throws IOException {
    if (instance == null) instance = new Lobby();
    }

     private Lobby () throws IOException {
        server = new ServerSocket(Meta.port);
        nitroServer = new ServerSocket(Meta.port+1);
        String msg = "Kasper:> Current memory usage of all stored data: ";
        System.out.println("Kasper:> Now ready to accept connections in port: " + Meta.port + ".");
        System.out.println("Kasper:> Device host: " + InetAddress.getLocalHost().getHostAddress());
    }
}
