import DataStructures.KasperCollection;
import DataStructures.KasperNode;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Parser.DiskIO;
import Persistence.InstantiatorService;
import Persistence.Outstream;
import Server.SuperClass.KasperGlobalMap;

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
        for (int i=0; i<100000; i++){
          users.addData("list"+i, new KasperList().addToList(marion, josh, josh, marion, josh));
        }

        Outstream out = new Outstream(node);
        out.bucketize();

      //  InstantiatorService.start();
     //   var x = KasperGlobalMap.getNode("dbbiodata").getCollection("users").getValue("marion@gmail.com").toMap();
       // System.out.println(x);





    }
}