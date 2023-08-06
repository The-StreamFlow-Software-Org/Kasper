/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


import com.kasper.beans.nio.streamflow.Connection;
import com.kasper.beans.nio.streamflow.Statement;
import com.kasper.commons.Network.Timer;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.exceptions.StreamFlowException;

import java.util.ArrayList;
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
    private static String linux = "172.18.180.86";
    public static String host = "localhost";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws StreamFlowException {
        try (Connection connection = new Connection(linux, "root", "streamflow")) {
           /* connection.prepareStatement("create node 'db3'; create collection 'users' in 'db3'").executeQuery().getNext();
            System.out.println("RESULT:" + connection.prepareStatement("insert ([]) in 'db3.users' as 'list';").executeQuery().getNext());
            Statement prepared = connection.prepareStatement("insert ? in ? as ?;");
            prepared.setObject(1, new KasperMap().put("This is a", "map"));
            prepared.setPath(2, "db3", "users", "list");
            prepared.setString(3, "tail");
            StringBuilder builder = new StringBuilder();
            Timer.getTimer().start();
            for (int i=0; i<100000; i++) {
                prepared.executeQuery();
            }
            */
            Timer.getTimer().start();
            var prepared = connection.prepareStatement("get 'db3.users.list';");
            var result = prepared.executeQuery().getNext();

         //   result.toMap();
            System.out.println("got with length " + result.toList().size() + " in " + Timer.getTimer().stop() + "s.");
        } catch (StreamFlowException e) {
            e.printStackTrace();
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