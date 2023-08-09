package computations;

import com.kasper.commons.Parser.PathParser;
import com.kasper.commons.datastructures.KasperList;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.exceptions.KasperException;
import com.kasper.commons.exceptions.NoSuchKasperObject;
import datastructures.KasperCollection;
import server.SuperClass.KasperGlobalMap;

public class NioDeleteResolver {

    public static void deleteObject (String path) {
        var object = KasperGlobalMap.findWithPath(path);
        if (object == null) throw new NoSuchKasperObject("entity", path);
        var parent = object.parent();
        if (parent == null) {
            KasperGlobalMap.globalmap.remove(path);
        } else {
            if (parent instanceof KasperList list) {
                String lastPart = path.replaceAll("^.*\\.", "");
                if (lastPart.equals("head")) {
                    list.popFirst();
                } else if (lastPart.equals("tail")){
                    list.popLast();
                } throw new KasperException("Invalid index provided, cannot resolve: " + lastPart);
            } else if (parent instanceof KasperMap) {
                String lastPart = path.replaceAll("^.*\\.", "");
                parent.toMap().remove(lastPart);
            } else {
                throw new KasperException("Invalid path provided, cannot resolve: " + path);
            }
        }
    }
}
