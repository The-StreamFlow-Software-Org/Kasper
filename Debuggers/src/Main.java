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
        KasperDocument document = KasperWriter.newDocument(new KasperAccessAuthenticator("kasper.util.key"));
        KasperList list = new KasperList().addToList("Sample", "args", "list");
        KasperObject obj = new KasperList().addToList("Hello", "this", "is", "a", "strin").addToList(new KasperMap().put("Make", "me").put("impressed", "mate"));
        list.addToList(obj, obj, obj, obj, obj, obj);
        document.setRequest("sample_key", list);
        KasperConstructor newObj = new KasperConstructor(document);
        KasperObject object = newObj.constructObject();
        System.out.println(object.castToList().toArray().get(4).toList());
        endTime();
        //System.out.println(object.toLi);
    }

    public static void startTime(){
        startTime = System.currentTimeMillis();
    }

    public static void endTime(){
        System.out.println("The execution time took " + ((System.currentTimeMillis() - startTime) / 1000.00 ) + " seconds");
    }
}