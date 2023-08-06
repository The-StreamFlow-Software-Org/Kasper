

import com.kasper.beans.nio.streamflow.DriverInstance;
import com.kasper.beans.nio.streamflow.DriverManager;
import com.kasper.beans.nio.streamflow.KasperStandardDriver;
import com.kasper.commons.exceptions.StreamFlowException;

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
        try (DriverInstance instance = DriverManager.getConnection("kasper://localhost:53182/root/streamflow")) {
            var result = instance.prepareStatement("get 'db';").executeQuery().checkQueryExceptions().getNext();
            System.out.println(result);

        }

    }







}