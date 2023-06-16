package KasperCommons.Parser;

import KasperCommons.DataStructures.KasperList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathParser {

    private ArrayList<String> list;
    public void addPath (String string) {
        list.add(0, string);
    }

    public PathParser (){
        list = new ArrayList<>();
    }

    public void addPathConventionally (String string) {
        list.add(string);
    }


    private  String parseString(String input) {
        // Replace all $ with $$
        String parsedString = input.replaceAll("\\$", "\\$\\$");

        // Replace all . with $.
        parsedString = parsedString.replace(".", "$.");


        return parsedString;
    }

    public  String parsePath() {
        StringBuilder build = new StringBuilder();
        for (int i=0; i<list.size(); i++){
            var x = list.get(i);
            build.append(parseString(x));
            if (i != list.size() - 1) build.append('.');
        }
        list = null;
        return build.toString();
    }

    public  List<String> unparsePath(String input) {
        String[] parts = input.split("(?<!\\$)\\.(?!\\$)");

        List<String> unparsedList = new ArrayList<>();
        for (String part : parts) {
            String unparseString = part.replace("$.", ".").replaceAll("\\$\\$", "\\$");
            unparsedList.add(unparseString);
        }

        return unparsedList;
    }
}
