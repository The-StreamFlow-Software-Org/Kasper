package Server.Handler;

import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Exceptions.NoSuchKasperObject;
import KasperCommons.Parser.PathParser;
import Server.SuperClass.KasperGlobalMap;

import java.util.Map;

public class PathCrawler {

    // iterate through the iterable object and find if they all have path 'withPath'.
    @SuppressWarnings("unchecked")
    public static KasperList getAllThatHasPath (String iterable, String withPath) {
        KasperList list = new KasperList().setId(iterable).castToList();
        var paths = PathParser.unparsePath(withPath);
        var baseObject = KasperGlobalMap.findWithPath(iterable);
        var iterableObject = baseObject.getIterable();
        try {
            for (var x : iterableObject) {
                // iterate through the  base object
                KasperObject currObject = null;
                if (x instanceof Map.Entry<?,?>) {
                    currObject = ((Map.Entry<String, KasperObject>) x).getValue();
                } else currObject = (KasperObject) x;
                var container = currObject;
                // iterate through inner paths
                try {
                    for (int i = 0; i < paths.size(); i++) {
                        currObject = accessElement(currObject, paths.get(i));
                    }
                    list.addToList(container);
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        return list;
    }

    // In the current object, try to access child path hasInstance.
    public static KasperObject accessElement (KasperObject currObject, String hasInstance) {
        if (currObject instanceof KasperMap m) {
            var x =  m.toMap().get(hasInstance);
            if (x == null) throw new RuntimeException();
            return x;
        } else {
            var list = currObject.toList();
            if (hasInstance.equals("head")) return list.getFirst();
            if (hasInstance.equals("tail")) return list.getLast();
            else throw new KasperException("Invalid position specified. Only 'head' and 'tail' access is allowed.");
        }
    }
}
