package KasperCommons.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathParser {

    private static ArrayList<String> list;
    public static void addPath (String string) {
        if (list == null) list = new ArrayList<>();
        list.add(0, string);
    }
    private static String parseString(String input) {
        // Replace all $ with $$
        String parsedString = input.replaceAll("\\$", "\\$\\$");

        // Replace all . with $.
        parsedString = parsedString.replace(".", "$.");


        return parsedString;
    }

    public static String parsePath() {
        StringBuilder build = new StringBuilder();
        for (int i=0; i<list.size(); i++){
            var x = list.get(i);
            build.append(parseString(x));
            if (i != list.size() - 1) build.append('.');
        }
        list = null;
        return build.toString();
    }

    public static List<String> unparsePath(String input) {
        String[] parts = input.split("(?<!\\$)\\.(?!\\$)");

        List<String> unparsedList = new ArrayList<>();
        for (String part : parts) {
            String unparseString = part.replace("$.", ".").replaceAll("\\$\\$", "\\$");
            unparsedList.add(unparseString);
        }

        return unparsedList;
    }
}
