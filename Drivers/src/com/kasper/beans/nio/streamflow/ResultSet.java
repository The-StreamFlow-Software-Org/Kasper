package com.kasper.beans.nio.streamflow;

import com.kasper.commons.datastructures.JSONUtils;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.datastructures.LocalPathCrawler;
import com.kasper.commons.exceptions.StreamFlowException;

import java.util.LinkedList;

public class ResultSet {
    public LinkedList<KasperObject> set;


    public ResultSet (String setString) throws StreamFlowException {
        try {
            set = JSONUtils.parseJson(setString).castToList().toArray();
        } catch (Exception e) {
            throw new StreamFlowException(e + "\n\tError at: " + setString);
        }
    }

    public boolean next() {
        try {
            if (set.peek() != null)
            return true;
            else return false;
        } catch (Exception e) {
            return false;
        }
    }

    public KasperObject getNext () throws StreamFlowException {
        try {
            var preStage = set.poll();
            if (preStage == null) throw new StreamFlowException("No more elements in the result set.");
            if (preStage instanceof KasperMap stagedObject) {
                assertException(stagedObject);
                var result = stagedObject.get("result");
                return result;
            } else {
                return preStage;
            }
        } catch (StreamFlowException x) {
            throw x;
        }  catch (Exception e) {
            throw new StreamFlowException(e);
        }

    }

    public void assertException (KasperMap map) throws StreamFlowException {
        var inside = map.get("exception");
        if (inside == null) return;
        throw new StreamFlowException(inside.toStr());
    }
}
