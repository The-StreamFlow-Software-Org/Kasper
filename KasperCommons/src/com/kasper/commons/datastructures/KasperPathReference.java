package com.kasper.commons.datastructures;

import com.kasper.commons.Parser.PathParser;

import java.io.Serial;

public class KasperPathReference extends KasperObject{
    protected PathParser parser;


    @Serial
    private static final long serialVersionUID = 4299160416314836746L;

    public KasperPathReference(String path) {
        super("reference");
        data = path;
        parser = new PathParser();
    }

    public KasperPathReference(PathParser parser) {
        super("reference");
        this.parser = parser;
        data = parser.parsePath();
    }

    public KasperPathReference addPath (String path){
        parser.addPath(path);
        data = parser.parsePath();
        return this;
    }
    public KasperPathReference addPathConventionally (String path){
        parser.addPathConventionally(path);
        data = parser.parsePath();
        return this;
    }


}
