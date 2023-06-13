package Persistence;

import DataStructures.KasperNode;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.Parser.KasperDocument;
import KasperCommons.Parser.KasperWriter;

public class Outstream {

    private KasperDocument document;

    public Outstream (KasperNode node) {
        this.document = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
        document.addFor("reconstruction");
        document.addArgs(document.createNode("node", node.getName()));
        var listOf = node.getData().entrySet();


    }



}
