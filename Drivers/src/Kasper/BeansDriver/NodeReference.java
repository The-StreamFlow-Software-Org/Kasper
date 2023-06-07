package Kasper.BeansDriver;

public class NodeReference extends AbstractReference{

    protected NodeReference(String nodeName, KasperBean serverInstance) {
        super("node");
        this.serverLocation = serverInstance.serverLocation;
        this.user = serverInstance.user;
        this.password = serverInstance.password;
        this.name = nodeName;
    }

    public CollectionReference useCollection (String referenceName) {
        return new CollectionReference(referenceName, this);
    }
}
