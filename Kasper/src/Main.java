import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperList;
import Persistence.InstantiatorService;
import Server.SuperClass.KasperGlobalMap;
import Server.SuperClass.Timer;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        new KasperAccessAuthenticator("kasper.util.key");
        Scanner scanner = new Scanner(System.in);
        favicon();
        Timer.getTimer().start();
        if (args[0] != null && args[0].equals("puts")){
            puts();
        }
        System.out.println("Kasper:> Staring instantiator service. Loading snapshots to memory.");
        InstantiatorService.start();
        System.out.println("Kasper:> Load from disk successful after " + Timer.getTimer().stop() + "s.");
        System.out.println("Kasper:> All objects loaded. All nodes are now ready for querying.");
        Timer.getTimer().reset();
        Timer.getTimer().start();
        scanner.nextLine();
        System.out.println("Kasper:> Terminating Kasper. Please wait while we 'bucketize' your data into snapshots.");
        InstantiatorService.close();
        System.out.println("Kasper:> Bucketizing has finished after " + Timer.getTimer().stop() + "s.");
        System.out.println("Press any key to finish...");
        scanner.nextLine();
        System.out.println("Kasper says bye!");





    }

    public static void puts(){
        KasperGlobalMap.newNode("f3").newCollection("prof");
        KasperGlobalMap.newNode("f9");
        var prof = KasperGlobalMap.getNode("f3").useCollection("prof");
        for (int i=0; i<1000000; i++){
          //  var putting = new KasperList().addToList("Algorithms", "Data Structures", "Operating Systems", "Database Management Systems", "Computer Networks", "Software Engineering", "Artificial Intelligence", "Computer Graphics", "Computer Security", "Web Development", "Mobile App Development", "Machine Learning", "Natural Language Processing", "Computer Vision", "Distributed Systems", "Computer Architecture", "Human-Computer Interaction", "Cryptography", "Computer Ethics", "Computer Organization");
            prof.put("prof" +i, "professor");
        }
    }

    public static void favicon(){
        System.out.println("\n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "                      ,╦╣╣▒▒╣@,                   ╓║▒▒▒▒@╖\n" +
                "                     ╔▒▒▒▒▒▒▒▒▒@               ,╖▒▒▒▒▒▒▒▒▒▒\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒             ╓╢▒▒▒▒▒▒▒▒▒▒▒▒[\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒U          ╖▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒U       ╓╢▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╜\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒U     ╓╢▒▒▒▒▒▒▒▒▒▒▒▒▒▒╢╜\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒U  ,╖▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╜`\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒U╓╢▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╜            ,╓╓,\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╜`          ,╖╢▒▒▒▒▒▒╖\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╜         ,╖╢▒▒▒▒▒▒▒▒▒▒▒\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╢╜       ╓╖╢▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╜      ╓╖╢▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╜    ╓╖╢▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒▒▒▒╜`     ╓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒▒▒╢╬╖     ║▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╣h    ╙╢▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╢╖    `╙╢▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╢╖      ╙╜▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╖,      `╙╢▒▒▒▒▒▒▒▒▒▒▒▒▒\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╢╖        ╙╜▒▒▒▒▒▒▒▒▒┘\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒ `╢▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╖          ╙╢▒▒▒╜`\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒    ╙▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╢╖\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒      ╙╢▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╖\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒         ╙▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒╖\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒           ╙╢▒▒▒▒▒▒▒▒▒▒▒▒▒▒\n" +
                "                     ▒▒▒▒▒▒▒▒▒▒▒             `╜▒▒▒▒▒▒▒▒▒▒▒▒[\n" +
                "                     ╙▒▒▒▒▒▒▒▒▒╜                ╙▒▒▒▒▒▒▒▒▒╢\n" +
                "                       ╙╜▒▒▒╜╜                    `╜╢▒▒╜╜`\n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "    \n" +
                "---\n");
    }
}