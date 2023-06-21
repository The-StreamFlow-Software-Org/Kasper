package Network;

import KasperCommons.Network.NetworkPackage;
import KasperCommons.Network.Timer;
import Persistence.InstantiatorService;
import KasperCommons.Authenticator.Meta;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.IOException;
import java.net.ServerSocket;

public class Lobby {
    private ServerSocket server;
    private static boolean ending = false;

    private static Lobby instance;

    public static void acceptConnections() throws Exception {
        init();
        System.out.println("Kasper:> Ready to accept connections.");


        Thread t = new Thread(()->{
            while (!ending) {
                try {
                    Thread.sleep(Meta.snapshotTimeout);
                    System.out.println("Kasper:> Writing snapshot to disk" + "...");
                    InstantiatorService.writeBackup();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t.start();


        Signal.handle(new Signal("INT"), new SignalHandler() {
            public void handle(Signal signal) {
                try {
                    ending = true;
                    System.out.println("Kasper:> Saving data snapshots...");
                    Timer.getTimer().start();
                    InstantiatorService.close();
                    System.out.println("Kasper:> Data snapshots saved after " + Timer.getTimer().stop() + "s.");
                    System.exit(0);
                } catch (Exception e) {
                    System.out.println("Kasper:> An exception occurred when saving the data snapshots. Please check the backups.");
                    System.exit(0);
                }
                System.exit(0);
            }
        });

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
