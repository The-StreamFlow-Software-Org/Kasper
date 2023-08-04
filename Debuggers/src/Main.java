/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


import com.kasper.beans.nio.streamflow.Connection;
import com.kasper.beans.nio.streamflow.Statement;
import com.kasper.commons.Network.Timer;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.datastructures.LockedLL;
import com.kasper.commons.exceptions.StreamFlowException;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

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
    public static void main(String[] args) throws StreamFlowException, InterruptedException {


        try (Connection connection = new Connection("localhost", "roots", "streamflow")) {
            connection.prepareStatement("create node 'x'; create collection 'y' in 'x';").executeQuery();
            var  statement = connection.prepareStatement("insert ? in 'x.y' as 'mitosis';");
            statement.setObject(1, new KasperMap().put("a", "b"));
            System.out.println(statement.executeQuery().getNext());
            System.out.println(connection.prepareStatement("get 'x.y.mitosis'").executeQuery().getNext());
        }


    }







    public static void multiThreadedTest() throws InterruptedException {
        Timer.getTimer().start();
        AtomicLong totalTime = new AtomicLong();
        ArrayList<Thread> t = new ArrayList<>();
        for (int i=0; i<30; i++) {
            t.add(new Thread(()->{
                try (Connection connection = new Connection("172.18.180.86", "root", "streamflow")) {
                    KasperObject x = null;
                    Timer timer = new Timer();
                    connection.prepareStatement("create node 'x'").executeQuery().getNext();
                    for (int j = 0; j < 1000; j++) {
                        x = connection.prepareStatement("create collection 'c' in 'x';").executeQuery().getNext();
                    }
                    System.out.println("Returned: " + x);
                    totalTime.addAndGet((long) timer.stop());
                } catch (StreamFlowException e) {}
            }));
        }

        for (var x : t) {
            x.start();
        }

        for (var x : t) {
            x.join();
        }

        System.out.println("Finished queries in: " + totalTime.get());


    }
}