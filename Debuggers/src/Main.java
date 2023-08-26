

import com.kasper.beans.nio.streamflow.DriverInstance;
import com.kasper.beans.nio.streamflow.DriverManager;
import com.kasper.beans.nio.streamflow.KasperStandardDriver;
import com.kasper.commons.datastructures.KasperInteger;
import com.kasper.commons.datastructures.KasperList;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.datastructures.KasperString;
import com.kasper.commons.exceptions.StreamFlowException;
import jdk.dynalink.linker.LinkerServices;

import java.sql.SQLException;
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws StreamFlowException {
        KasperString string = new KasperString("Hello World");
        System.out.println(string.equals(new KasperString("Hello World")));
        try (DriverInstance instance =
                     DriverManager.getConnection("kasper://localhost:53182/root/streamflow")){
           instance.prepareStatement("create node 'cit';" +
                    "create collection 'users' in 'cit';" +
                    "insert ? in 'cit.users' as 'hernah'")
                    .setObject(1, new KasperString("Hernah")).executeQuery().getNext();
            System.out.println(instance.prepareStatement("get 'cit.users.hernah'").executeQuery().getNext());
        }

    }







}