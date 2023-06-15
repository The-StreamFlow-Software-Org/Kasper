import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import Persistence.InstantiatorService;
import Server.SuperClass.KasperGlobalMap;
import Server.SuperClass.Meta;
import Server.SuperClass.Timer;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        new KasperAccessAuthenticator("kasper.util.key");
        Scanner scanner = new Scanner(System.in);
        favicon();
        Timer.getTimer().start();
        handleargs(args);
        System.out.println("Kasper:> Staring instantiator service. Loading snapshots to memory.");
        InstantiatorService.start();
        System.out.println("Kasper:> Load from disk successful after " + Timer.getTimer().stop() + "s.");
        System.out.println("Kasper:> All objects loaded. All nodes are now ready for querying.");
        Timer.getTimer().reset();
        Timer.getTimer().start();
        //scanner.nextLine();
        System.out.println("Kasper:> Terminating Kasper. Please wait while we 'bucketize' your data into snapshots.");
        InstantiatorService.close();
        System.out.println("Kasper:> Bucketizing has finished after " + Timer.getTimer().stop() + "s.");
        System.out.println("Press any key to finish...");
        //scanner.nextLine();
        System.out.println("Kasper says bye!");


    }


    public static void sample(){
        var comment = new KasperList().addToList("Hello", "World");
        var user = new KasperMap().put("comments", comment).put("name", "rufelle");
        KasperGlobalMap.getNode("sampledb").useCollection("samples").put("comments", comment).put("user", user);

    }



    private static void handleargs (String[] args){

        if (args.length == 0) return;



        // for puts debug command
        if (args.length > 0 && args[0].equals("puts")){
            System.out.println("KasperCLI:> Initiating puts.");
            int removal = 1;
            if (args.length > 1) {
                removal = 2;
                try {
                    Meta.sample = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    System.out.println("KasperCLI:> Invalid sample size. Fallback to default size: " + Meta.sample);
                }
            }
            puts();
            handleargs(removeArg(args, removal));
            return;
        }


        if (args[0].equals("--savepath")) {
            Meta.changePath(args[1]);
            handleargs(removeArg(args, 2));
            return;
        }


            System.out.print("KasperCLI:> Invalid args sequence: ");
            for (var x : args){
                System.out.print(x + " ");
            }
            System.out.println("\nKasperCLI:> Kasper cannot process your command. Kasper says bye!");
            System.exit(1);


        if (args.length < 2) {
            System.out.println("KasperCLI:> Invalid command, " + args[0] + " must have at least one argument");
            return;
        }

    }

    public static String[] removeArg (String[] args, int n){
        String[] newargs = null;
        try {
            newargs= new String[args.length - n];
            if (args.length - n <= 0 ){
                return new String[0];
            }
            int j = 0;
            for (int i=n; i< args.length; i++){
                newargs[j] = args[i];
                j++;
            }
        }
        catch (Exception exception){
            System.exit(0);
        } return newargs;

    }

    public static void puts(){
        KasperGlobalMap.newNode("f3").newCollection("prof");
        KasperGlobalMap.newNode("f9");
        var prof = KasperGlobalMap.getNode("f3").useCollection("prof");
        for (int i=0; i<Meta.sample; i++){
          //  var putting = new KasperList().addToList("Algorithms", "Data Structures", "Operating Systems", "Database Management Systems", "Computer Networks", "Software Engineering", "Artificial Intelligence", "Computer Graphics", "Computer Security", "Web Development", "Mobile App Development", "Machine Learning", "Natural Language Processing", "Computer Vision", "Distributed Systems", "Computer Architecture", "Human-Computer Interaction", "Cryptography", "Computer Ethics", "Computer Organization");
            prof.put("prof" +i, new KasperList().addToList("professor", "professor", "professor", "professor", "professor", "professor", "professor", "professor", "professor", "professor"));
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