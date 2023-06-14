import DataStructures.KasperCollection;
import DataStructures.KasperNode;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Parser.DiskIO;
import Persistence.InstantiatorService;
import Persistence.Outstream;
import Server.Concurrent.ItertableRunnable;
import Server.Concurrent.Pool;
import Server.SuperClass.KasperGlobalMap;

import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) throws Exception {
        new KasperAccessAuthenticator("kasper.util.key");

        KasperNode node = new KasperNode("dbbiodata");
        var users = new KasperCollection(node, "users");
        node.addCollection(users);
        var marion = new KasperMap()
                .put("fname", "Marion")
                .put("lname", "Buhat")
                .put("age", "20")
                .put("gender", "Male");

        var josh = new KasperMap()
                .put("fname", "Josh")
                .put("lname", "Dimpas")
                .put("age", "19")
                .put("gender", "Male");

        users.addData("marion@gmail.com", marion)
                .addData("jdimpas@gmail.com", josh)
                .addData("dup", marion)
                .addData("list", new KasperList().addToList(marion, josh, josh, marion, josh));
        for (int i=0; i<100; i++){
            Pool.newThread(new ItertableRunnable(i) {
                @Override
                public void run() {
                    for (int j=0; j<1000; j++) {
                        users.addData("list" + index + j, new KasperList().addToList(marion, josh, josh, marion, josh));
                        users.addData("klazz" + index + j, KasperObject.str("emman"));
                    }
                }
            });
        }
        Pool.shutdown();



        // Outstream out = new Outstream(node);
        // out.bucketize();



        InstantiatorService.start();
        Scanner scan = new Scanner(System.in);
        var x = KasperGlobalMap.getNode("dbbiodata").getCollection("users").getValue("marion@gmail.com").toMap();
        System.out.println(x);
        scan.nextLine();
        System.out.println(KasperGlobalMap.getNode("dbbiodata").getCollection("users").getValue("jdimpas@gmail.com").toMap());
        System.out.println(KasperGlobalMap.getNode("dbbiodata").getCollection("users").getValue("klazz99999").toStr());





    }
}