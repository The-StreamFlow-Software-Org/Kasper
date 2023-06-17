import Kasper.BeansDriver.DataStructures.KasperBean;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Network.Timer;

import java.net.Socket;

public class BeansMain {
    public static void main(String[] args) {
        KasperBean bean = new KasperBean("localhost", "root", "");
        new KasperAccessAuthenticator("kasper.util.key");
        var collection = bean.useNode("f1").useCollection("prof");


        // generate a new object


        KasperObject sirSerato = new KasperMap().put("name", "Jay Vince Serato")
                .put("subjets", new KasperList().addToList("Data Structures",
                        "Discrete Math", "Design and Analysis of Algorithms"));
        System.out.println(collection.getKey("sirserato"));

        // put to DB

      //  collection.setKey("sirserato", sirSerato);
      //  System.out.println("Hello, " + sir.get("name") + ".\nYour subjects are: " + sir.get("subjets"));





    }
}
