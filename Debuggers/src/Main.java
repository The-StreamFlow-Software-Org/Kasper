import KasperCommons.Authenticator.PacketOuterClass;
import KasperCommons.Authenticator.Meta;
import KasperCommons.DataStructures.KasperList;
import Persistence.InstantiatorService;
import Server.SuperClass.KasperGlobalMap;


public class Main {
    private static long startTime;
    public static void main(String[] args) throws Exception {
        InstantiatorService.start();
        var res = KasperGlobalMap.findWithPath("CS000111.new1.head").toList();
        System.out.println(res);
        InstantiatorService.close();

    }

    public static void puts(){
        KasperGlobalMap.newNode("f3").newCollection("prof");
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