import com.kasper.beans.datastructures.KasperBean;
import com.kasper.commons.datastructures.KasperList;
import com.kasper.commons.Network.Timer;

import java.util.Scanner;

public class BeansMain {
    public static void main(String[] args) {
        KasperBean conn = new KasperBean("192.168.254.103", "", "");
        var collection = conn.createNode("test2").createCollection("collection");
        KasperList list = new KasperList();
        System.out.println("Kasper results: ");
        Timer.getTimer().start();
        for (int i=0; i<100000; i++) {
            collection.setKey(Integer.toString(i), Integer.toString(i));
        }
        System.out.println("Set took: " + Timer.getTimer().stop());
        Timer.getTimer().start();
        for (int i=0; i<100000; i++) {
            collection.getKey(Integer.toString(i));
        }
        System.out.println("Get took: " + Timer.getTimer().stop());
        new Scanner(System.in).nextLine();

    }
}
