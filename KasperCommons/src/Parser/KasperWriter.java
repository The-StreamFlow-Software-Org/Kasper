package Parser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class KasperWriter {
    /*
    The KasperWriter class is a helper class that can help generate documents.
    Used for sending over commands, both by the Java Driver (Beans) and the
    Kasper server.
     */
    private DocumentBuilderFactory builderFactory;
    private DocumentBuilder build;


    private static KasperWriter instance = null;

    private static KasperWriter getInstance () {
        if (instance == null) {
            instance = new KasperWriter();
        } return instance;
    }

    private KasperWriter (){
        try {
            builderFactory = DocumentBuilderFactory.newInstance();
            build = builderFactory.newDocumentBuilder();
        } catch (Exception ignored){}
    }


    // Use only this method to generate a new Kasper Document
    public static KasperDocument newDocument(){
        var mThis = getInstance();
        return new KasperDocument(mThis.build);
    }



}
