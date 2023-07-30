package com.kasper.beans.nio.streamflow;

import com.kasper.commons.datastructures.JSONUtils;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.exceptions.StreamFlowException;

import java.util.LinkedList;

public class ResultSet {
    public LinkedList<KasperObject> set;


    public ResultSet (String setString) throws StreamFlowException {
        try {
            set = JSONUtils.parseJson(setString).castToList().toArray();
        } catch (Exception e) {
            throw new StreamFlowException(e);
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
            var stagedObject = set.poll().castToMap();
            assertException(stagedObject);
            return stagedObject.get("result");
        } catch (StreamFlowException x) {
            throw x;
        }  catch (Exception e) {
            throw new StreamFlowException(e);
        }

    }

    public void assertException (KasperMap map) throws StreamFlowException {
        var inside = map.get("exception");
        if ( inside == null) return;
        throw new StreamFlowException(inside.toStr());
    }
}
