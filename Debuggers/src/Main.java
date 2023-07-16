/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import com.kasper.beans.datastructures.CollectionReference;
import com.kasper.beans.datastructures.KasperBean;
import parser.ParseProcessor;
import parser.executor.DatabaseOperations;
import parser.executor.ExecutionQueue;

import java.util.ArrayList;

/**
 *
 * @author Rufelle
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ParseProcessor.consumeString("INSERT (\"Hello, World\") IN 'hello.world' AS 'fighter';");
    }


    public static void stressTest() {
        ArrayList<Thread> threads = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            threads.add(new Thread(() -> {
                var head = new KasperBean("", "", "");
                var createMode = false;
                CollectionReference conn = null;
                if (createMode) {
                    conn = head.createNode("strong").createCollection("man");
                    conn.setKey("hello", "world");
                } else {
                    conn = head.useNode("strong").useCollection("man");
                    conn.updateKey("hello", "world");
                }
                for (int i = 0; i < 100; i++) {
                    System.out.println("load");
                    var temp = new KasperBean("", "", "");
                    System.out.println("strong");
                    var strong = head.useNode("strong");
                    System.out.println("man");
                    var collection = strong.useCollection("man");
                    conn.updateKey("hello", Integer.toString(i));
                    collection.getKey("hello");
                    System.out.println(conn.getKey("hello"));
                }

            }));
        }

        for (var x : threads) {
            x.start();

        }
    }
}
