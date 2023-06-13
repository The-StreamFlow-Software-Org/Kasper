import DataStructures.KasperCollection;
import DataStructures.KasperNode;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperObject;
import Persistence.Outstream;
import Server.Concurrent.Pool;

public class Main {
    public static void main(String[] args) {
        new KasperAccessAuthenticator("kasper.util.key");
        KasperNode node = new KasperNode("sample_node");
        KasperCollection collection = new KasperCollection(node, "sample_collection");
        collection.addData("Hello", KasperObject.str("Test")).addData("World", KasperObject.str("Test ra"));
        node.addCollection("coLLECTION Test", collection);
        Outstream outstream = new Outstream();
        Outstream outstream1 = new Outstream();
        outstream1.serializeNode(node);
        outstream.serializeNode(node);
        outstream.chain(outstream1);
        System.out.println(outstream.construct());


    }
}