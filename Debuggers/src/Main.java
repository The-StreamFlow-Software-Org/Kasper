/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


import com.kasper.beans.nio.streamflow.Connection;
import com.kasper.commons.Network.Timer;
import com.kasper.commons.datastructures.LockedLL;
import com.kasper.commons.exceptions.StreamFlowException;

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
        Connection connection  = new  Connection("localhost", "root", "streamflow");
        var prepared = connection.prepareStatement("create node ?;");
        prepared.setString(1, "dbOne");
        Timer.getTimer().start();
        for (int i=0; i<10000; i++) {
            prepared.executeQuery().getNext();
        }
        System.out.println("Finished in " + Timer.getTimer().stop());
        stressTest();
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

    public static int totalRequests = 10000;
    public static double time =0;
    private static int finished = 0;
    private synchronized static void incrementTime (double time) {
        time += time;
    }
    public static void incrementFinished() {
        finished++;
        if (finished >= totalRequests - 5) {
            System.out.println("Finished in " + Timer.getTimer().stop());
            System.out.println("Num errors: " + totalErrors);
        }
    }

    public static void stressTest() {
        ArrayList<Thread> t = new ArrayList<>();
        for (int i=0; i<totalRequests; i++) {
            t.add(new Thread(()->{
                try {
                    Connection connection = new Connection("localhost", "root", "streamflow");
                    incrementFinished();
                } catch (StreamFlowException e) {
                    System.out.println("Caught an error.");
                    incrementErrors();
                    Timer time = new Timer();
                    incrementFinished();
                }
            }));
        }
        Timer.getTimer().start();
        t.stream().forEach((x)->x.start());

    }
}
