import Kasper.BeansDriver.DataStructures.KasperBean;
import KasperCommons.Authenticator.Meta;
import KasperCommons.DataStructures.JSONUtils;
import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import KasperCommons.DataStructures.LocalPathCrawler;
import KasperCommons.Network.Timer;
import Server.SuperClass.KasperGlobalMap;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;


public class Main {

    protected static Random random = new Random();
    private static long startTime;
    public static void main(String[] args) throws IOException {
        System.out.println("Establishing a connection with KasperEngine...");
        KasperBean conn = new KasperBean("localhost", "", "");
        var collection = conn.createNode("test3").createCollection("collection");
        KasperList list = new KasperList();
        System.out.println("Kasper results: ");
        var listInt = new KasperList();
        for (int i=0; i<1000000; i++) {
            listInt.addToList("wash");
        }
        Timer.getTimer().start();
        collection.setKey("listInt", listInt);
        System.out.println("Set took: " + Timer.getTimer().stop());
        Timer.getTimer().start();
        var res= collection.tryGetKey(conn.generateRawReference("test3.collection.listInt"));
        System.out.println("Get took: " + Timer.getTimer().stop());
        new Scanner(System.in).nextLine();
        System.out.println(res);
        System.out.println(res.get().toList().size());
       // System.out.println(res.get().toMap().get("listInt").toList().size());
        collection.clear();
    }

    public static void benchmark() throws IOException {
        Random random = new Random();
        // InstantiatorService.start();
        KasperList list = new KasperList();
        var map = new KasperMap().put("top-level", list);
        for (int i=0; i<100000; i++) {
        //    var li = generateRandomSubjects(random);
          //  list.addToList(new KasperMap().put("name", newName(random)).put("sub", li));
        } map.put("final", "final");
        var newObj = JSONUtils.parseJson(JSONUtils.objectToJsonStream(map));
        LocalPathCrawler.crawlPaths(newObj);
        LocalPathCrawler.finalPathSetter(newObj, "testpath");
        var mapper = newObj.toMap().get("final");
        Timer.getTimer().start();
        System.out.println(mapper.getPath());
        System.out.println("Serialization overhead with protocol buffers: "+ Timer.getTimer().stop() + "s" );
        System.out.println(mapper);
        new Scanner(System.in).nextLine();
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


    private static final String[] NAMES = {
            "Aria", "Felix", "Liam", "Evelyn", "Leo", "Sophia", "Mason", "Amelia", "Elijah", "Harper", "Logan", "Charlotte", "Noah", "Emma", "Carter", "Ava", "Ethan", "Mia", "Henry", "Grace", "Herald", "Hark", "Earl", "Quinn", "Luna", "Archer", "Fiona", "Gavin", "Isabelle", "Jasper", "Mila", "Oliver", "Nora", "Parker", "Violet", "Wyatt", "Zara", "Xander", "Yara", "Caleb", "Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Henry", "Ivy", "Jack", "Josh", "Gian", "Janessa", "Mikka", "Ashmor", "Jetan", "Nize", "Marion", "Ericka"
            // Add more names as needed
    };

    public static String newName(){
        StringBuilder build = new StringBuilder();
        build.append(NAMES[random.nextInt(0, NAMES.length-1)]);
        build.append(NAMES[random.nextInt(0, NAMES.length-1)]);
        build.append(NAMES[random.nextInt(0, NAMES.length-1)]);
        return build.toString();
    }

    private static final String[] SUBJECTS = {
            "Computer Networks", "Database Systems", "Operating Systems", "Artificial Intelligence", "Software Engineering",
            "Computer Graphics", "Data Mining", "Machine Learning", "Algorithms", "Web Development", "Computer Security",
            "Data Structures", "Network Security", "Cloud Computing", "Human-Computer Interaction", "Computer Architecture",
            "Big Data Analytics", "Software Testing", "Information Retrieval", "Cryptography"
            // Add more subjects as needed
    };

    private static KasperList generateRandomSubjects() {
        KasperList subjects = new KasperList();

        int numSubjects = random.nextInt(SUBJECTS.length) + 1; // Generate 1 to 4 subjects

        for (int i = 0; i < numSubjects; i++) {
            String randomSubject = SUBJECTS[random.nextInt(SUBJECTS.length)];
            subjects.addToList(randomSubject);
        }

        return subjects;
    }
}