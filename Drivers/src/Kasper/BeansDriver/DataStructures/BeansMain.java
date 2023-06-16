package Kasper.BeansDriver.DataStructures;

import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.Network.Timer;

public class BeansMain {
    public static void main(String[] args) {
        KasperBean bean = new KasperBean("localhost", "root", "");
        new KasperAccessAuthenticator("kasper.util.key");
        var collection = bean.useNode("f1").useCollection("prof");
        for (int i=0; i<2; i++){
            Timer.getTimer().start();
            var x = collection.getKey("serato");
            System.out.println("Finished query in " + Timer.getTimer().stop() + "s.");
            System.out.println("Output: " + x.toMap() + "\nwith subjects: " + x.toMap().get("subjects").toList());
            Timer.getTimer().start();
            var y = collection.getKey("tulin");
            System.out.println("Finished query in " + Timer.getTimer().stop() + "s.");
            System.out.println("Output: " + y.toMap() + "\nwith subjects: " + y.toMap().get("subjects").toList());
        }
    }
}
