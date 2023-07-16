package parser.executor;

import java.util.HashMap;

public interface ExecutionUnit {
    void databaseTask(HashMap<String, Object> arguments);
}
