import DataStructures.KasperList;
import DataStructures.KasperMap;
import DataStructures.KasperObject;
import DataStructures.KasperString;
import Parser.KasperDocument;
import Parser.KasperWriter;
import Parser.Manifest;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        var document = KasperWriter.newDocument();
        var arr = new ArrayList<String>();
        arr.add("Hello");
        arr.add("World");
        arr.add("String");
        var list = KasperObject.createBasicList(arr);
        var advanced_list = new KasperList();
        advanced_list.addToList(KasperObject.createString(arr.get(2)));
        advanced_list.addToList(list);
        KasperMap map = new KasperMap();
        map.put("key", list);
        map.put("value", advanced_list);

        document.authRequest("root", "");
        System.out.println(document);
    }
}