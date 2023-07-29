package nio.kasper;

import com.kasper.commons.datastructures.JSONUtils;
import com.kasper.commons.datastructures.KasperList;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.datastructures.KasperObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class StagedResultSet {
    KasperMap resultSet = new KasperMap();


    public StagedResultSet (ArrayList<KasperObject> result) {
        KasperList listResults = new KasperList();
        result.stream().forEach(x ->  {
            if(x != null) listResults.addToList(x);
        });
        resultSet.put("result", listResults);
    }

    public StagedResultSet (Exception e) {
        resultSet.put("exception", e.getMessage());
    }

    public byte[] getBytes () {
        String data = JSONUtils.objectToJsonStream(resultSet);
        return data.getBytes(StandardCharsets.UTF_16);
    }
}
