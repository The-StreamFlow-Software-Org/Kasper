/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import com.kasper.beans.datastructures.CollectionReference;
import com.kasper.beans.datastructures.KasperBean;
import com.kasper.beans.streamflow.Connection;
import com.kasper.beans.streamflow.Statement;
import com.kasper.commons.datastructures.JSONUtils;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.datastructures.LockedLL;
import parser.ParseProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Rufelle
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ParseProcessor process = new ParseProcessor();
        // TODO: buggy handling with quotes in parenthesis
       // process.consumeString("match @correct(?)");
        Connection connection = new Connection();
        Statement statement = connection.prepareStatement("INSERT (?) IN ? AS ?");
        KasperMap map = new KasperMap().put("Hello", "World").put("This is", "me!");
        statement.setObject(1,map);
        statement.setPath(2, "nodeDB", "helloWorld");
        statement.setString(3, "hello");
        System.out.println(statement.peekQuery());
        process.consumeString(statement.peekQuery());


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


    public static void stressTest() {
        ArrayList<Thread> threads = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
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

     //   System.exit(0);
    }
}
