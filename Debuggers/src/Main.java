import Kasper.BeansDriver.DataStructures.CollectionReference;
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
    public static void main(String[] args) throws IOException {
        KasperBean conn = new KasperBean("localhost", "", "");
        boolean startMode = false;
        CollectionReference collection = null;
        if (startMode) collection = conn.createNode("test3").createCollection("collection");
        else collection = conn.useNode("test3").useCollection("collection");
        collection.updateKey("adv", new KasperMap().put("hello", "world").put("0", "not an index"));
        collection.deleteKey(collection.generatePathReference("adv", "0"));
        System.out.println(collection.getKey(conn.generaterawPathReference("test3.collection")));

    }

}