package DataStructures;

import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Parser.PathParser;
import Server.SuperClass.KasperGlobalMap;
import org.w3c.dom.Node;

import java.io.Serial;
import java.util.Map;
import java.util.Set;

public class KasperNode extends KasperServerAbstracts {

    @Serial
    private static final long serialVersionUID = 3405473397893997156L;

    public KasperNode(String name) {
        super("node");
        this.name = name;
    }




    public Set<Map.Entry<String, KasperObject>> iterate (){
        return data.entrySet();
    }


    public KasperNode (Node nodeConstructor) throws InterruptedException {
        super("node");
        var constChild = nodeConstructor.getChildNodes();
        var collections = constChild.item(1).getChildNodes();
        this.name = constChild.item(0).getTextContent();
        for (int i=0; i<collections.getLength(); i++){
            addCollection(new KasperCollection(collections.item(i), this));
        }
    }


    private KasperNode addCollection (KasperCollection value){
        data.put(value.getName(), value);
        return this;
    }

    public KasperCollection newCollection (String name){
        var collect = new KasperCollection(this, name);
        addCollection(collect);
        return collect;
    }

    public KasperCollection useCollection(String name){
        return (KasperCollection) get(name);
    }

    @Override
    public Map<String, KasperObject> toMap() {
        return data;
    }

    public KasperNode addCollection (String key, KasperCollection value){
        data.put(key, value);
        return this;
    }

    public KasperObject find (String ... args) {
        PathParser pathParser = new PathParser();
        for (int x = args.length-1; x >= 0 ; x--) {
            pathParser.addPath(args[x]);
        } pathParser.addPath(name);
        return KasperGlobalMap.findWithPath(pathParser.parsePath());
    }


}
