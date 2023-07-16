package parser.executor;

import com.kasper.commons.datastructures.KasperObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseOperations {
    public static void create(HashMap<String, Object> arguments) {
        KasperObject toCreate = (KasperObject) arguments.get("object");
        ArrayList<String> path = (ArrayList<String>) arguments.get("path");
        String key = (String) arguments.get("key");

        // do here
    }
}
