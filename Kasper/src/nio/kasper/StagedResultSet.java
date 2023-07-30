package nio.kasper;

import com.kasper.commons.datastructures.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/*
    This class appropriately divides the result of a result set
    into specific protocol-compliant data structures.

    To view the result set StreamFlow standard, see the KasperCommons project,
    and view the __impl__standard__result_set__ file.

    This class is used by the KasperServer component to transform the result set
    into a protocol-compliant data structure.
 */



public class StagedResultSet {
    KasperList resultSet = new KasperList();


    public StagedResultSet (ArrayList<KasperEntity> result) {
        result.stream().forEach(x ->  {
            if(x.getIntType() == KasperEntity.TYPE_OBJECT) resultSet.addToList(new KasperMap().put("result", x.getObject()));
            else if (x.getIntType() == KasperEntity.TYPE_QUERY_OK) resultSet.addToList(x.getObject());
            else if (x.getIntType() == KasperEntity.TYPE_EXCEPTION) resultSet.addToList(new KasperMap().put("exception", x.getObject()));
        });
    }

    public StagedResultSet () {}

    public void addResult (KasperEntity x) {
        if(x.getIntType() == KasperEntity.TYPE_OBJECT) resultSet.addToList(new KasperMap().put("result", x.getObject()));
        else if (x.getIntType() == KasperEntity.TYPE_QUERY_OK) resultSet.addToList(x.getObject());
        else if (x.getIntType() == KasperEntity.TYPE_EXCEPTION) resultSet.addToList(new KasperMap().put("exception", x.getObject()));
    }

    public void addQueryOk() {
        resultSet.addToList(KasperEntity.QueryOk.storedInstance());
    }


    public byte[] getBytes () {
        String data = JSONUtils.objectToJsonStream(resultSet);
        return data.getBytes(StandardCharsets.UTF_16);
    }
}