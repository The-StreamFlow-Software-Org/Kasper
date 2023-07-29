package com.kasper.beans.nio.streamflow;

import com.kasper.commons.datastructures.JSONUtils;
import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.exceptions.PreparedQueryException;

import java.util.ArrayList;

public class Statement {
    private Connection connection;
    private ArrayList<String> brokenQuery;
    private int index = 0;
    private ArrayList<String> parameters;
    private String queryString;


    protected Statement (Connection implementingConnection, String query) {
        Check.notNull(query, "string");
        this.connection = implementingConnection;
        this.brokenQuery = new ArrayList<>();
        this.parameters = new ArrayList<>();
        processQuery(query);
    }

    protected void processQuery(String query){
        StringBuilder queryBuilder = new StringBuilder();
        for (int i=0; i<query.length(); i++) {
            if (query.charAt(i) == '?') {
                index++;
                parameters.add("?");
                brokenQuery.add(queryBuilder.toString());
                queryBuilder.setLength(0);
            } else {
                queryBuilder.append(query.charAt(i));
            }
        }
    }

    public Statement setString (int index, String str) {
        StringBuilder builder = new StringBuilder();
        builder.append('"');
        Check.indexValidity(this.index, index);
        // parse this str string to be a valid string literal where -> '"' will be turned into '\"
        for (int i=0; i<str.length(); i++) {
            if (str.charAt(i) == '"') {
                builder.append("\\\"");
            } else {
                builder.append(str.charAt(i));
            }
        }
        builder.append('"');
        parameters.set(index-1, builder.toString());
        return this;
    }

    public Statement setObject (int index, KasperObject obj) {
        Check.indexValidity(this.index, index);
        parameters.set(index-1, JSONUtils.objectToJsonStream(obj));
        return this;
    }

    public Statement setInt (int index, int num) {
        Check.indexValidity(this.index, index);
        parameters.set(index-1, String.valueOf(num));
        return this;
    }

    public Statement setDouble (int index, double num) {
        Check.indexValidity(this.index, index);
        parameters.set(index-1, String.valueOf(num));
        return this;
    }

    public Statement setPath (int index, String ... path) {
        Check.indexValidity(this.index, index);
        StringBuilder bigPath = new StringBuilder();
        // iterate through the path, if there is '.' then make it delimited to '\.'
        for (var x : path) {
            StringBuilder smallPath = new StringBuilder();
            for (int i = 0; i<x.length(); i++) {
                if (x.charAt(i) == '.') {
                    smallPath.append("\\.");
                } else {
                    smallPath.append(x.charAt(i));
                }
            }
            bigPath.append(smallPath).append('.');
        }
        bigPath.deleteCharAt(bigPath.length()-1);
        return setString(index, bigPath.toString());
    }

    private void buildQuery(){
        try {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < index; i++) {
                builder.append(brokenQuery.get(i));
                builder.append(parameters.get(i));
            }
            queryString = builder.toString();
        } catch (IndexOutOfBoundsException e) {
            throw new PreparedQueryException("Not all parameters are set");
        }
    }

    public String peekQuery () {
        buildQuery();
        return queryString;
    }
}
