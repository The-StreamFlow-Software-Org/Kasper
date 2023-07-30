/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import com.kasper.beans.datastructures.CollectionReference;
import com.kasper.beans.datastructures.KasperBean;
import com.kasper.beans.nio.streamflow.Connection;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.datastructures.LockedLL;
import com.kasper.commons.exceptions.StreamFlowException;
import network.Pool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Rufelle
 */
public class Main {

    private static int totalErrors = 0;

    public synchronized static void incrementErrors() {
        totalErrors++;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws StreamFlowException {
        ArrayList<Thread> t = new ArrayList<>();
        for (int i=0; i<1000; i++) {
            t.add(new Thread(()->{
                try {
                    Connection connection = new Connection("localhost", "root", "streamflow");
                    connection.close();
                } catch (StreamFlowException e) {
                    System.out.println("Caught an error.");
                    incrementErrors();
                }
            }));
        }
        t.stream().forEach((x)->x.start());
        t.stream().forEach((x)-> {
            try {
                x.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("Num errors: " + totalErrors);
    }

    public static void simpleThreadTest(){
        KasperBean bean = new KasperBean("", "", "");
        for (int i=0; i<1000; i++) {
            bean.useNode("me");
        }
        System.out.println("DONE!");
    }


    public static void threadTest(){
        LockedLL<Integer> list = new LockedLL<>();
        LinkedList<Thread> threadList = new LinkedList<>();
        for (int i=0; i<10; i++) {
            final int index = i;
            threadList.add(new Thread(
                    ()->{
                     //   System.out.println("Thread execute..." + index);
                        list.add(index);
                        list.add(index);
                        list.remove(0);
                        System.out.println(list);
                       // System.out.println("Thead finish..."  +index);
                    }
            ));

        }

        for (var x:threadList) {
            x.start();
        }

    }

    public static void singleThread(){
        var bean = new KasperBean("localhost", "helloWorld", "hello");
        boolean createMode = true;
        CollectionReference conn = null;
        if (createMode) {
            conn = bean.createNode("node").createCollection("conn");
        } else {
            conn = bean.useNode("node").useCollection("conn");
        }
        conn.setKey("map", new KasperMap());
        for (int i=0; i<100000; i++) {
            conn.setKey(conn.generatePathReference("map"), Integer.toString(i), "inside u");
        }
        System.out.println(conn.getKey("map"));
    }


    public static void stressTest() {
        ArrayList<Thread> threads = new ArrayList<>();
        for (int x = 0; x < 1; x++) {
            threads.add(new Thread(() -> {
                var head = new KasperBean("", "", "");
                var createMode = true
                        ;
                CollectionReference conn = null;
                if (createMode) {
                    conn = head.createNode("strong").createCollection("man");
                    conn.setKey("hello", "world");
                } else {
                    conn = head.useNode("strong").useCollection("man");
                    conn.updateKey("hello", "world");
                }
                for (int i = 0; i < 10; i++) {
                    System.out.println("load");
                    var temp = new KasperBean("", "", "");
                    System.out.println("strong");
                    var strong = head.useNode("strong");
                    System.out.println("man");
                    var collection = strong.useCollection("man");
                    try {
                        collection.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //  conn.updateKey("hello", Integer.toString(i));
                }

            }));
        }

        for (var x : threads) {
            x.start();
        }
        Pool.shutdown();

     //   System.exit(0);
    }
}
