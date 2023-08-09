package com.kasper.beans.nio.streamflow;

import com.kasper.commons.aliases.Method;
import com.kasper.commons.datastructures.JSONUtils;
import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.exceptions.KasperException;
import com.kasper.commons.exceptions.PreparedQueryException;
import com.kasper.commons.exceptions.StreamFlowException;

import java.util.ArrayList;

public class Statement {
    private KasperStandardDriver kasperStandardDriver;
    private ArrayList<String> brokenQuery;
    private int index = 0;
    private ArrayList<String> parameters;
    private ArrayList<Boolean> alreadySet;
    private String queryString;


    protected Statement (KasperStandardDriver implementingDriver, String query) {
        Check.notNull(query, "string");
        this.kasperStandardDriver = implementingDriver;
        this.brokenQuery = new ArrayList<>();
        this.parameters = new ArrayList<>();
        this.alreadySet = new ArrayList<>();
        processQuery(query);
    }

    protected void processQuery(String query){
        StringBuilder queryBuilder = new StringBuilder();
        for (int i=0; i<query.length(); i++) {
            if (query.charAt(i) == '?') {
                index++;
                parameters.add("?");
                alreadySet.add(false);
                brokenQuery.add(queryBuilder.toString());
                queryBuilder.setLength(0);
            } else {
                queryBuilder.append(query.charAt(i));
            }
        } if (!queryBuilder.isEmpty())brokenQuery.add(queryBuilder.toString());
    }


    /**
     * Sets the '?' at the specified index as a string. This only escapes the '"' character.
     *  @param index the specified index to replace. Index starts at 1.
     *  @param str the string to insert at the query.
     *  @return returns this Statement instance. Builder pattern.
     */
    public Statement setString (int index, String str) throws PreparedQueryException {
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
        alreadySet.set(index-1, true);
        return this;
    }

    /**
     * Sets the '?' at the specified index as an object. It turns a KasperObject into a JSON string encapsulated by '()'.
     * @param index index the specified index to replace. Index starts at 1.
     * @param obj the KasperObject to send to the server.
     * @return returns this Statement instance. Builder pattern.
     */
    public Statement setObject (int index, KasperObject obj) throws PreparedQueryException {
        Check.indexValidity(this.index, index);
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        builder.append(JSONUtils.objectToJsonStream(obj));
        builder.append(')');
        parameters.set(index-1, builder.toString());
        alreadySet.set(index-1, true);
        return this;
    }

    /**
     * Sets the '?' at the specified index as a number.
     * @param index the specified index to replace. Index starts at 1.
     * @param num the number to insert at the query.
     * @return returns this Statement instance. Builder pattern.
     */
    public Statement setInt (int index, int num) throws PreparedQueryException {
        Check.indexValidity(this.index, index);
        parameters.set(index-1, String.valueOf(num));
        alreadySet.set(index-1, true);
        return this;
    }

    /**
     * Sets the '?' at the specified index as a decimal.
     * @param index the specified index to replace. Index starts at 1.
     * @param num the number to insert at the query.
     * @return returns this Statement instance. Builder pattern.
     */
    public Statement setDouble (int index, double num) throws PreparedQueryException {
        Check.indexValidity(this.index, index);
        parameters.set(index-1, String.valueOf(num));
        alreadySet.set(index-1, true);
        return this;
    }

    /**
     * Sets the '?' at the specified index as a number.
     * @param index the specified index to replace. Index starts at 1.
     * @param path this is a variadic args argument. This constructs a path starting from the most significant unit. For example, if the path is 'db.hello.user', this will be inputted as (index, "db", "hello", "user"). This also escapes periods properly in paths.
     * @return returns this Statement instance. Builder pattern.
     */
    public Statement setPath (int index, String ... path) throws PreparedQueryException {
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
        alreadySet.set(index-1, true);
        return setString(index, bigPath.toString());
    }


    private void buildQuery() throws PreparedQueryException {
        try {
            if (index == 0) {
                queryString = brokenQuery.get(0);
                return;
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < index; i++) {
                builder.append(brokenQuery.get(i));
                builder.append(parameters.get(i));
            } if (brokenQuery.size() > index) builder.append(brokenQuery.get(index));
            queryString = builder.toString();
        } catch (IndexOutOfBoundsException e) {
            throw new PreparedQueryException("Not all parameters are set");
        }
        alreadySet.stream().forEach(x->{
            if (!x) try {
                throw new PreparedQueryException("Not all parameters are set.");
            } catch (PreparedQueryException e) {
                throw new KasperException(e);
            }
        });

    }

    protected String peekQuery () throws PreparedQueryException {
        buildQuery();
        return queryString;
    }

    /**
     * Executes the query and sends it to the database.
     * @return a result set.
     * @throws StreamFlowException
     */
    public synchronized ResultSet executeQuery() throws StreamFlowException {
        buildQuery();
        var result = kasperStandardDriver.wire.writeAndGetBytes(Method.QUERY, queryString);
        return new ResultSet(result.second(), result.first());
    }

    /**
     * Introspects the query at the current instance. Ensure that all the parameters are inserted.
     * @return the query string at this current instance. It
     */
    public String introspectQuery() throws PreparedQueryException {
        buildQuery();
        return queryString;
    }
}
