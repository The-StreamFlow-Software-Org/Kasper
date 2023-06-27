package Network;

import KasperCommons.Authenticator.Meta;
import KasperCommons.Concurrent.Pool;
import KasperCommons.Network.KasperNitroWire;
import KasperCommons.Network.Timer;
import Persistence.InstantiatorService;
import Server.SuperClass.KasperGlobalMap;
import org.openjdk.jol.info.GraphLayout;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.IOException;
import java.net.ServerSocket;

public class Lobby {
    private ServerSocket server;
    private ServerSocket nitroServer;
    private static boolean ending = false;

    private static Lobby instance;

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


        Signal.handle(new Signal("INT"), new SignalHandler() {
            public void handle(Signal signal) {
                try {
                    ending = true;
                    System.out.println("Kasper:> Saving data snapshots...");
                    Timer.getTimer().start();
                    InstantiatorService.close();
                    System.out.println("Kasper:> Data snapshots saved after " + Timer.getTimer().stop() + "s.");
                    System.out.println("Kasper says bye! :)");
                    System.exit(0);
                } catch (Exception e) {
                    System.out.println("Kasper:> An exception occurred when saving the data snapshots. Please check the backups.");
                    System.exit(0);
                }
                System.exit(0);
            }
        });

        while (true) {
            var initWire = (new KasperNitroWire(instance.server.accept()));
            var nitroSocket = instance.nitroServer.accept();
            initWire.setNitro(nitroSocket);
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
        Pool.newThread(()->{
            while (true) {
                System.out.println(msg + GraphLayout.parseInstance(KasperGlobalMap.globalmap).totalSize() + " bytes.");
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
