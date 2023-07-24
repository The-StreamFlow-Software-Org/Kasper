package com.kasper.beans.streamflow;

import com.kasper.commons.exceptions.KasperException;
import com.kasper.commons.exceptions.PreparedQueryException;

public class Check {

    public static void indexValidity (int index, int passedIn) {
        if (index < passedIn) throw new PreparedQueryException("Index out of bounds, max index was " + index + " but passed in " + passedIn + ".");
    }

    public static void notNull (Object o, String type) {
        if (o == null) throw new KasperException("Null value passed in for " + type + ".");
    }
}
