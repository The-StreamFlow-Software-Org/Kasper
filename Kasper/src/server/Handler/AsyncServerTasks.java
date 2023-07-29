package server.Handler;

import com.kasper.commons.Network.Timer;
import com.kasper.commons.authenticator.Meta;
import com.kasper.commons.debug.W;
import network.Pool;
import nio.kasper.Orchestrator;
import sun.misc.Signal;

public class AsyncServerTasks {

    private static boolean ending = false;

    public static void persistenceSnapshots () {
        Thread t = new Thread(()->{
            while (!ending) {
                try {
                    Thread.sleep(Meta.snapshotTimeout);
                    System.gc();
                    W.rite("Persistence snapshots are being written to disk.");

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Pool.newThread(t);
    }

    public static void exitHandler (Orchestrator orchestrator) {
        final boolean[] doubleSIGINT = {false};


        Signal.handle(new Signal("INT"), (Signal sig)-> {
            try {
                if (doubleSIGINT[0]) {
                    System.exit(0);
                } doubleSIGINT[0] = true;
                ending = true;
                System.out.println("Kasper:> Instantiating the closing service. To force close the server, use 'ctrl + c' again. Warning: This may cause data loss.");
                orchestrator.stop();
                System.out.println("Kasper:> Data snapshots saved after " + Timer.getTimer().stop() + "s.");
                System.out.println("Kasper says bye! :)");
                System.exit(0);
            } catch (Exception e) {
                System.out.println("Kasper:> An exception occurred when saving the data snapshots. Please check the backups.");
                System.exit(0);
            }
            System.exit(0);
        });
    }
}
