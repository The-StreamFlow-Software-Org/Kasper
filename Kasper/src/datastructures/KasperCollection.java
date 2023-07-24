package datastructures;

import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.Parser.KasperConstructor;
import com.kasper.commons.Parser.PathParser;
import server.SuperClass.KasperGlobalMap;
import org.w3c.dom.Node;

import java.io.Serial;
import java.util.Map;
import java.util.Set;

public class KasperCollection extends KasperServerAbstracts {


    @Serial
    private static final long serialVersionUID = 1216153714206946182L;

    public KasperCollection(KasperNode parent, String name) {
        super("collection");
        this.name = name;
        this.parent = parent;
    }

    public KasperCollection (Node thisNode, KasperNode parent) throws InterruptedException {
        super("collection");
        this.thisNode = thisNode;
        this.parent = parent;
        var meta = thisNode.getChildNodes();
        this.name = meta.item(0).getTextContent();
        var data = thisNode.getChildNodes().item(1);
            var entries = data.getChildNodes();
        var x = this;
        for (int i=0; i<entries.getLength(); i+=2){
            var key = entries.item(i).getTextContent();
            var constructor = KasperConstructor.constructNode(entries.item(i+1));
            x.put(key, constructor);
        }


    }


    public KasperCollection put(String key, KasperObject value) {
        data.put(key, value);
        return this;
    }


    public KasperCollection put (String key, String value){
        return put(key, KasperObject.str(value));
    }

    public Set<Map.Entry<String, KasperObject>> iterate () {
        return data.entrySet();
    }

    public KasperObject getValue (String key) {
        return get(key);
    }


    public KasperObject find (String ... args){
        PathParser parse = new PathParser();
        for (int x = args.length-1; x >= 0 ; x--) {
            parse.addPath(args[x]);
        } parse.addPath(name);
        parse.addPath(((KasperNode)parent).getName());
        return KasperGlobalMap.findWithPath(parse.parsePath());

    }

    @Override
    public Map<String, KasperObject> toMap() {
        return data;
    }


}
