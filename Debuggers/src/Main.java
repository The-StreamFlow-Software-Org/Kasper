import Kasper.BeansDriver.DataStructures.CollectionReference;
import Kasper.BeansDriver.DataStructures.KasperBean;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import KasperCommons.DataStructures.KasperReference;
import KasperCommons.DataStructures.KasperString;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Parser.KasperDocument;
import KasperCommons.Parser.KasperWriter;
import Persistence.InstantiatorService;
import Server.Handler.RequestHandler;
import Server.SuperClass.KasperGlobalMap;
import Server.SuperClass.Meta;


public class Main {
    private static long startTime;
    public static void main(String[] args) throws Exception {
        var document = KasperWriter.newDocument(new KasperAccessAuthenticator("kasper.util.key"));
        var newDocument = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());

        InstantiatorService.start();
        puts();
        if (false){
            puts();
            System.exit(0);
        }
        KasperList fave = new KasperList().addToList("banana", "pineapple", "orange");
        document.setRequest("f3.prof", "myfaves", fave);
        KasperMap person = new KasperMap().put("name", "Alexis").put("myfaves", new KasperReference("f3.prof.myfaves"));
        newDocument.setRequest("f3.prof", "Alexis", person);
        RequestHandler.handleQuery(document);
        RequestHandler.handleQuery(newDocument);
        System.out.println(KasperGlobalMap.findWithPath("f3.prof.Alexis").toMap().get("myfaves").toList());


    }

    public static void puts(){
        KasperGlobalMap.newNode("f3").newCollection("prof");
        KasperGlobalMap.newNode("f9");
        var prof = KasperGlobalMap.getNode("f3").useCollection("prof");
        for (int i = 0; i< Meta.sample; i++){
            //  var putting = new KasperList().addToList("Algorithms", "Data Structures", "Operating Systems", "Database Management Systems", "Computer Networks", "Software Engineering", "Artificial Intelligence", "Computer Graphics", "Computer Security", "Web Development", "Mobile App Development", "Machine Learning", "Natural Language Processing", "Computer Vision", "Distributed Systems", "Computer Architecture", "Human-Computer Interaction", "Cryptography", "Computer Ethics", "Computer Organization");
            prof.put("prof" +i, new KasperList().addToList("professor", "professor", "professor", "professor", "professor", "professor", "professor", "professor", "professor", "professor"));
        }
    }

    public static void startTime(){
        startTime = System.currentTimeMillis();
    }

    public static void endTime(){
        System.out.println("The execution time took " + ((System.currentTimeMillis() - startTime) / 1000.00 ) + " seconds");
    }
}