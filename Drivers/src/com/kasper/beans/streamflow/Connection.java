package com.kasper.beans.streamflow;

public class Connection {

    public Connection () {
        // TODO: implement Connection class
    }

    public Statement prepareStatement(String statement){
        return new Statement(this, statement);
    }
}
