package Kasper.BeansDriver;

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
}
