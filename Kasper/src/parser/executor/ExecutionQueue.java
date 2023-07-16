package parser.executor;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

public class ExecutionQueue {
    protected Queue<ExecutionUnit> processes;
    protected Queue<HashMap<String, Object>> arguments;


    public ExecutionQueue () {
        processes = new ArrayDeque<>();
    }

    public void addProcess (ExecutionUnit unit, HashMap<String, Object> argument) {
        processes.add(unit);
        arguments.add(argument);
    }

    public void executeAndDispose() {
        while (!processes.isEmpty()) {
            var unit = processes.poll();
            var args = arguments.poll();
            unit.databaseTask(args);
        }
    }
}
