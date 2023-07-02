import Kasper.BeansDriver.DataStructures.CollectionReference;
import Kasper.BeansDriver.DataStructures.KasperBean;
import KasperCommons.DataStructures.KasperMap;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        KasperBean conn = new KasperBean("localhost", "", "");
        boolean startMode = false;
        CollectionReference collection = null;
        if (startMode) collection = conn.createNode("test3").createCollection("collection");
        else collection = conn.useNode("test3").useCollection("collection");
        collection.updateKey("adv", new KasperMap().put("hello", "world").put("0", "not an index"));
        collection.deleteThis();
        System.out.println(collection.getKey(conn.generateRawPathReference("test3.collection")));

    }

}