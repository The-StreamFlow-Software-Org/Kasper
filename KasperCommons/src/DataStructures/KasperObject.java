package DataStructures;

import Parser.KasperDocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KasperObject {
    private String type;
    protected Object data;

    public String getType (){
        return type;
    }

    protected KasperObject (String type) {
        this.type = type;
    }

    public ArrayList<KasperObject> toList (){
        return (ArrayList) data;
    }

    public String toString (){
        return (String) data;
    }

    public Map toMap (){
        return (Map) data;
    }

    public static KasperString createString(String str){
        return new KasperString(str);
    }

    public static KasperList createBasicList(ArrayList<String> str) {
        var list = new KasperList();
        for (var x : str) {
            list.addToList(createString(x));
        } return list;
    }




}
