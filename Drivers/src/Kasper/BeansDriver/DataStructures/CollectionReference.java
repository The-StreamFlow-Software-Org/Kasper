package Kasper.BeansDriver.DataStructures;

import Kasper.BeansDriver.Listeners.QueryListener;

public class CollectionReference extends AbstractReference{
    protected NodeReference parent;
    protected String nodeName;

    protected CollectionReference(String name, NodeReference parent) {
        super("collection");
        this.parent = parent;
        password = parent.password;
        serverLocation = parent.serverLocation;
        user = parent.serverLocation;
        nodeName = parent.name;
        this.name = name;
    }

    public QueryListener getKey(String keyName){
        return new QueryListener(null);
    }


}
