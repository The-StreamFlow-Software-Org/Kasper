package parser.executor;

import com.kasper.Boost.Pair;
import com.kasper.commons.exceptions.KasperException;
import nio.kasper.StagedResultSet;
import stateholder.functions.StoredProcedures;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class ExecutionQueue {


    // static execution Queue saved units

    protected Queue<Pair<String, HashMap<String, Object>>> processes;
    protected StagedResultSet resultSet;

    public StagedResultSet resultSet() {
        return resultSet;
    }

    public ExecutionQueue () {
        processes = new LinkedList<>();
        resultSet = new StagedResultSet();
    }

    public void addProcess (String unit, HashMap<String, Object> argument) {
        processes.add(new Pair<>(unit, argument));
    }

    public void executeAndDispose() {
        while (!processes.isEmpty()) {
            try {
                var coupled = processes.poll();
                var unit = coupled.first();
                var args = coupled.second();
                var result = StoredProcedures.execute(unit, args);
                resultSet.addResult(result);
            } catch (KasperException e) {
                resultSet.addResult(e);
            } catch (Exception e) {
                resultSet.addResult(new KasperException(e));
            }
        }
    }
}
