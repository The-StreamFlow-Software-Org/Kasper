package com.kasper.commons.datastructures;

import com.kasper.Boost.Pair;
import com.kasper.commons.annotations.Dangerous;

import java.util.concurrent.ConcurrentHashMap;

public class KasperRelationship extends KasperMap {
    protected ConcurrentHashMap<String, Pair<String, KasperObject>> data = new ConcurrentHashMap<>();


    public KasperRelationship() {
        super("relationship");
        this.parent = parent;
    }

    protected ConcurrentHashMap<String, Pair<String, KasperObject>> data() {
        return data;
    }

    public KasperObject getRelationship(String id) {
        return data().get(id).second();
    }

    protected String getRelationshipPath(String id) {
        return data().get(id).first();
    }

    public Pair<String, KasperObject> getRelationshipSet(String id) {
        return data().get(id);
    }


    @Dangerous
    public ConcurrentHashMap<String, Pair<String, KasperObject>> toRelationshipMap() {
        return data();
    }


}
