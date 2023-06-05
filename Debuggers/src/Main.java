import DataStructures.KasperList;
import DataStructures.KasperMap;
import DataStructures.KasperObject;
import DataStructures.KasperString;
import Parser.KasperDocument;
import Parser.KasperWriter;

public class Main {
    public static void main(String[] args) {
        KasperList root = new KasperList();
        root.addToList("Hello", "World", "It's", "Me").addToList(new KasperMap()
                .put("Hello", "root").put("World", "Lol"), new KasperString("Stringify this"));
        KasperDocument document = KasperWriter.newDocument();
        document.setRequest("key", root);
        System.out.println(document);

    }
}