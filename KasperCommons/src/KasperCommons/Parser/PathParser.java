package KasperCommons.Parser;

import java.util.ArrayList;

public class PathParser {

    public ArrayList<String> pathMeta;
    public void addPath (String string) {
        pathMeta.add(0, string);
    }


    public PathParser (){
        pathMeta = new ArrayList<>();
    }

    public void addPathConventionally (String string) {
        pathMeta.add(string);
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
        for (int i = 0; i< pathMeta.size(); i++){
            var x = pathMeta.get(i);
            build.append(parseString(x));
            if (i != pathMeta.size() - 1) build.append('.');
        }
        return build.toString();
    }

    public static ArrayList<String> unparsePath(String input) {
        String[] parts = input.split("(?<!\\$)\\.(?!\\$)");

        ArrayList<String> unparsedList = new ArrayList<>();
        for (String part : parts) {
            String unparseString = part.replace("$.", ".").replaceAll("\\$\\$", "\\$");
            unparsedList.add(unparseString);
        }

        return unparsedList;
    }
}
