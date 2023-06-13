import Kasper.BeansDriver.DataStructures.CollectionReference;
import Kasper.BeansDriver.DataStructures.KasperBean;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Parser.KasperConstructor;
import KasperCommons.Parser.KasperDocument;
import KasperCommons.Parser.KasperWriter;





public class Main {
    private static long startTime;
    public static void main(String[] args) throws KasperException {
        startTime();
        KasperBean bean = new KasperBean("localhost", "root", "");
        CollectionReference users = bean.useNode("dbNize").useCollection("users");
        users.getKey("nize@gmail.com").addOnFailureListener( (x)-> {
            System.out.println("An excpetion occured with name " + x.toString());
        }).addOnSuccessListener(x -> {
            System.out.println("Got users!" + x.toMap().get("username").toStr());
        });
    }

    public static void startTime(){
        startTime = System.currentTimeMillis();
    }

    public static void endTime(){
        System.out.println("The execution time took " + ((System.currentTimeMillis() - startTime) / 1000.00 ) + " seconds");
    }
}