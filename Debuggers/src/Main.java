/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import Kasper.BeansDriver.DataStructures.CollectionReference;
import Kasper.BeansDriver.DataStructures.KasperBean;

import java.io.IOException;
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
        ArrayList<Thread> runnables = new ArrayList<>();
        for (int j=0; j<1000; j++){
            Thread t = new Thread(()-> {
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
                System.out.println("->>");
            });
            runnables.add(t);
        }
        for (var t : runnables) {
            t.start();
        }
    }


}
