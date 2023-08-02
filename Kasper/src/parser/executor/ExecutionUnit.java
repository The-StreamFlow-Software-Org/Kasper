package parser.executor;

import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.exceptions.KasperException;

import java.util.HashMap;

public interface ExecutionUnit {
    KasperObject databaseTask(HashMap<String, Object> arguments) throws KasperException;
}
