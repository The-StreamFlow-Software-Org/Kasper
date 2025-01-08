

import com.kasper.beans.nio.streamflow.*;
import com.kasper.commons.datastructures.*;
import com.kasper.commons.exceptions.StreamFlowException;
import datastructures.KasperCollection;
import jdk.dynalink.linker.LinkerServices;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;


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


    public static void main(String[] args) throws StreamFlowException {
       DriverInstance driver = DriverManager.getConnection("kasper://monorail.proxy.rlwy.net:40147/root/streamflow");
        driver.prepareStatement("delete node 'tasker'").executeQuery().getNext();

       var obj = driver.prepareStatement("get *").executeQuery().getNext();
       // driver.prepareStatement("delete *").executeQuery().getNext();
        System.out.println(obj.toMap().keySet());

    }
}