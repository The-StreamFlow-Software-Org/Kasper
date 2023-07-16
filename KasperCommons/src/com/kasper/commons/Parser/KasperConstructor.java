package com.kasper.commons.Parser;

import com.kasper.commons.datastructures.KasperList;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.datastructures.KasperString;
import com.kasper.commons.exceptions.KasperException;
import com.kasper.commons.Handlers.ExceptionHandler;
import com.kasper.commons.Network.Timer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class KasperConstructor {
    private Node args;
    private Node base_value;
    private Document document;
    private Node purpose;

    public static void checkForExceptions (String string) throws KasperException {
        var document = KasperDocument.constructor(string).document;
        var exception = document.getElementsByTagName("exception").item(0);
        if (!exception.getTextContent().isEmpty()) {
            ExceptionHandler.attemptException(exception.getAttributes().getNamedItem("exception_type").getTextContent(), exception.getTextContent());
            throw new KasperException(exception.getTextContent());
        }
    }

    public KasperConstructor(KasperDocument kasperDocument){
        this.document = kasperDocument.document;

        // exception checking
        var exception = document.getElementsByTagName("exception").item(0);
        if (!exception.getTextContent().isEmpty()) {
            ExceptionHandler.attemptException(exception.getAttributes().getNamedItem("exception_type").getTextContent(), exception.getTextContent());
        }

        args = document.getElementsByTagName("args").item(0);
        purpose = document.getElementsByTagName("for").item(0);
    }

    public static KasperObject constructNode (Node n) {
        return recursivelyConstruct(n);
    }


    public KasperObject constructObject (){

        if (purpose.getTextContent().equals("response") && args.getTextContent().equals("ok"))
            return null;
        Timer t = new Timer();
        t.start();
        var returnval = recursivelyConstruct(args.getChildNodes().item(0));
        System.out.println("Object Construction Overhead :" + t.stop());
        return returnval;
    }





    private static KasperObject recursivelyConstruct (Node currNode){
        var type = currNode.getNodeName();
        var element = currNode.getAttributes().getNamedItem("path").getTextContent();
        var list = currNode.getChildNodes();
        if (type.equals("string")) {
            return new KasperString(currNode.getTextContent()).setId(element);
        }
        else if (type.equals("list")){
            var objList = new KasperList().setId(element).castToList();
            for (int i = 0; i<list.getLength(); i++){
                objList.addToList(recursivelyConstruct(list.item(i)));
            } return objList;
        } else {
            var objMap = new KasperMap().setId(element).castToMap();
            for (int i=0; i<list.getLength(); i+=2){
                var keyNode = list.item(i+1);
                var valueNode = keyNode.getChildNodes().item(0);
                var keyName = list.item(i);
                objMap.put(keyName.getTextContent(), recursivelyConstruct(valueNode));
            } return objMap;
        }
    }
}