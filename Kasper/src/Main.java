import com.kasper.commons.Handlers.LogWriter;
import com.kasper.commons.authenticator.KasperAccessAuthenticator;
import com.kasper.commons.authenticator.Meta;
import com.kasper.commons.Network.Timer;
import Persistence.InstantiatorService;
import nio.kasper.Orchestrator;
import server.SuperClass.GlobalHolders;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
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

    // instantiates the KasperEngine with the appropriate global variables.
    public static void init(String[] args) throws Exception {
        favicon();
        Timer.getTimer().start();
        System.out.println("Kasper:> Loading Meta Variables. Change the Meta variables in kasper/data/kasper.init");
        System.out.println("Kasper:> Staring instantiator service. Loading snapshots to memory.");
        InstantiatorService.start();
        System.out.println("Kasper:> Load from disk successful after " + Timer.getTimer().stop() + "s.");
        System.out.println("Kasper:> All objects loaded. All nodes are now ready for querying.");
        Timer.getTimer().reset();
        Orchestrator orchestrator = new Orchestrator();
        orchestrator.start();
    }


    public static void favicon(){
        System.out.println("                                        \n" +
                "                                        \n" +
                "         .::..         ....             \n" +
                "        .:::::      ..:::...            \n" +
                "        .:::::    ..::::::.             \n" +
                "        .:::::  .:::::::..              \n" +
                "        .:::::.:::::::.      ...        \n" +
                "        .:::::::::::.    ..:::::.         Kasper DB \n" +
                "        .:::::::::.  ..:::::::::.         version " + Meta.version + "\n" +
                "        .:::::::.  .::::::::::::.         (c) ruff.io, 2023\n" +
                "        .::::::::. .::::::::::::.         Rufelle Emmanuel Pactol\n" +
                "        .::::::::::. ..:::::::::.         kasperdocs.netlify.app\n" +
                "        .::::::::::::.   ..:::::.       \n" +
                "        .:::::.::::::::.    ....        \n" +
                "        .:::::   .:::::::.              \n" +
                "        .:::::     .:::::::.            \n" +
                "        .:::::       .:::::.            \n" +
                "          ...          ....             \n" +
                "                                        \n" +
                "                                        \n");
    }
}