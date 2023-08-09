

import com.kasper.beans.nio.streamflow.DriverInstance;
import com.kasper.beans.nio.streamflow.DriverManager;
import com.kasper.beans.nio.streamflow.KasperStandardDriver;
import com.kasper.commons.datastructures.KasperInteger;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.exceptions.StreamFlowException;

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
        boolean createMode = false;
        try (DriverInstance instance = DriverManager.getConnection("kasper://localhost:53182/root/streamflow")) {
            // instance.prepareStatement("create node 'x'; create collection 'y' in 'x';").executeQuery().checkQueryExceptions();
            instance.prepareStatement("insert ([]) in 'x.y' as 'list';").executeQuery().checkQueryExceptions();
            instance.prepareStatement("delete entity in 'x.y.list';").executeQuery().checkQueryExceptions();
            System.out.println(instance.prepareStatement("get 'x'").executeQuery().checkQueryExceptions().getNext());
        }

    }







}