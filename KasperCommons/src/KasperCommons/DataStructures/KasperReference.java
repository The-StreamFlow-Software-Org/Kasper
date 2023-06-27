package KasperCommons.DataStructures;

import KasperCommons.Parser.PathParser;

import java.io.Serial;
import java.util.ArrayList;

public class KasperReference extends KasperObject{
    protected PathParser parser;


    @Serial
    private static final long serialVersionUID = 4299160416314836746L;

    public KasperReference(String path) {
        super("reference");
        data = path.intern();
        parser = new PathParser();
    }

    public KasperReference(PathParser parser) {
        super("reference");
        this.parser = parser;
        data = parser.parsePath();
    }

    public KasperReference addPath (String path){
        parser.addPath(path);
        data = parser.parsePath();
        return this;
    }
    public KasperReference addPathConventionally (String path){
        parser.addPathConventionally(path);
        data = parser.parsePath();
        return this;
    }


}
