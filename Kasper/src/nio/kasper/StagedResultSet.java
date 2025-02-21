package nio.kasper;

import com.kasper.commons.Network.Timer;
import com.kasper.commons.datastructures.*;

import java.nio.charset.StandardCharsets;


/*
    This class appropriately divides the result of a result set
    into specific protocol-compliant data structures.

    To view the result set StreamFlow standard, see the KasperCommons project,
    and view the __impl__standard__result_set__ file.

    This class is used by the KasperServer component to transform the result set
    into a protocol-compliant data structure.
 */



public class StagedResultSet {

    KasperList resultSet = null;


    Timer timer = new Timer();
    public StagedResultSet () {
        resultSet = new KasperList();
    }

    public void addResult (KasperEntity x) {
        if (x == null) {
            resultSet.addToList(KasperEntity.QueryOk.storedInstance());
            return;
        }
        if(x.getIntType() == KasperEntity.TYPE_OBJECT){
            var map = new KasperMap().put("result", x.getObject());
            resultSet.addToList(map);
        }
        else if (x.getIntType() == KasperEntity.TYPE_QUERY_OK) resultSet.addToList(x.getObject());
        else if (x.getIntType() == KasperEntity.TYPE_EXCEPTION) resultSet.addToList(new KasperMap().put("exception", x.getObject()));
    }

    public void addQueryOk() {
        resultSet.addLast(KasperEntity.QueryOk.storedInstance());
    }


    public byte[] getBytes () {
        String data = JSONUtils.objectToJsonStream(resultSet);
        return data.getBytes(StandardCharsets.UTF_8);
    }
}
