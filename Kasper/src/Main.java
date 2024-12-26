import com.kasper.commons.Handlers.LogWriter;
import com.kasper.commons.authenticator.KasperAccessAuthenticator;
import com.kasper.commons.authenticator.Meta;
import com.kasper.commons.Network.Timer;
import Persistence.InstantiatorService;
import nio.kasper.Orchestrator;
import server.SuperClass.GlobalHolders;

import java.io.Console;
import java.util.Scanner;

public class Main {
    // private static final Scanner scanner = new Scanner(System.in);
    // BEGIN FUNCTION


    public static void main(String[] args) throws Exception {
       KasperRoutine.start(args);
    }

    public static class KasperRoutine {

        public static void start(String[] args){
            try {
                GlobalHolders.argv = args;
                GlobalHolders.argc = args.length;
                init(args);
            } catch (Exception e) {
                if (InstantiatorService.lockedByThis) InstantiatorService.unlockThisServer();
                System.out.println("CRITICAL ERROR:> Kasper Engine failed to continue serving. Please check the logs for more details.");
                LogWriter.writeLog(e);
            }
        }
    }

    // instantiates the KasperEngine with the appropriate global variables.
    public static void init(String[] args) throws Exception {
        Timer.getTimer().start();
        System.out.println("Kasper:> [Server Context] Loading Meta Variables. Change the Meta variables in kasper/data/kasper.init");
        System.out.println("Kasper:> [Persistence] Staring instantiator service. Loading snapshots to memory.");
        InstantiatorService.start();
        System.out.println("Kasper:> [Persistence] Load from disk successful after " + Timer.getTimer().stop() + "s.");
        System.out.println("Kasper:> [General Message] All objects loaded. All nodes are now ready for querying.");
        Timer.getTimer().reset();
        Orchestrator orchestrator = new Orchestrator();
        orchestrator.start();
    }


    static {
        System.out.println("                                        \n" +
                "                                        \n" +
                "         .::..         ....             \n" +
                "        .:::::      ..:::...            \n" +
                "        .:::::    ..::::::.             \n" +
                "        .:::::  .:::::::..              \n" +
                "        .:::::.:::::::.      ...        \n" +
                "        .:::::::::::.    ..:::::.         Kasper DB [Community Edition]\n" +
                "        .:::::::::.  ..:::::::::.         Version [" + Meta.version + "]\n" +
                "        .:::::::.  .::::::::::::.         (c) The StreamFlow Software Organization, 2023\n" +
                "        .::::::::. .::::::::::::.         (c) Rufelle Emmanuel Pactol\n" +
                "        .::::::::::. ..:::::::::.         rufelle.com\n" +
                "        .::::::::::::.   ..:::::.         rufelleemmanuel.pactol@cit.edu\n" +
                "        .:::::.::::::::.    ....        \n" +
                "        .:::::   .:::::::.              \n" +
                "        .:::::     .:::::::.            \n" +
                "        .:::::       .:::::.            \n" +
                "          ...          ....             \n" +
                "                                        \n" +
                "                                        \n");
    }

}