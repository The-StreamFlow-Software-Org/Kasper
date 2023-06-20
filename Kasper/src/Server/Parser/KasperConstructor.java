package Server.Parser;

import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.DataStructures.KasperString;
import KasperCommons.Network.Timer;
import KasperCommons.Parser.KasperDocument;
import Server.SuperClass.KasperGlobalMap;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class KasperConstructor {
    private Node args;
    private Node base_value;
    private Document document;

    public KasperConstructor(KasperDocument kasperDocument){
        this.document = kasperDocument.document;
        args = document.getElementsByTagName("args").item(0);
    }

    public static KasperObject constructNode (Node n) {
        return recursivelyConstruct(n);
    }



    public static KasperObject constructNode (Node n, String paths) {
        Timer t = new Timer();
        t.start();
        var nodes =  recursivelyConstruct(n, paths);
        System.out.println("Construction from n -> obj : " + t.stop());
        return nodes;
    }


    public KasperObject constructObject (){
        var nodes = args.getChildNodes();
        var values = nodes.item(1);
        base_value = values.getChildNodes().item(0);
        return recursivelyConstruct(base_value);
    }



    private static KasperObject recursivelyConstruct (Node currNode, String pathPointer){
        var type = currNode.getNodeName();
        if (type.equals("string")) {
            return new KasperString(currNode.getTextContent()).setPath(pathPointer);
        }
        else if (type.equals("list")){
            var list = currNode.getChildNodes();
            var objList = new KasperList().setPath(pathPointer).castToList();
            for (int i = 0; i<list.getLength(); i++){
                objList.addToList(recursivelyConstruct(list.item(i), pathPointer+ "." + i));
            } return objList;
        } else if (type.equals("reference")){
            return KasperGlobalMap.findWithPath(currNode.getTextContent());
        }

        var list = currNode.getChildNodes();
        var objMap = new KasperMap().setPath(pathPointer).castToMap();
        for (int i=0; i<list.getLength(); i+=2){
            var keyNode = list.item(i+1);
            var valueNode = keyNode.getChildNodes().item(0);
            var keyName = list.item(i);
            objMap.put(keyName.getTextContent(), recursivelyConstruct(valueNode, pathPointer+"."+keyName.getTextContent()));
        } return objMap;
    }





    private static KasperObject recursivelyConstruct (Node currNode){
        var type = currNode.getNodeName();
        if (type.equals("string")) {
            return new KasperString(currNode.getTextContent());
        }
        else if (type.equals("list")){
            var list = currNode.getChildNodes();
            var objList = new KasperList();
            for (int i = 0; i<list.getLength(); i++){
                objList.addToList(recursivelyConstruct(list.item(i)));
            } return objList;
        } else if (type.equals("reference")){
            return KasperGlobalMap.findWithPath(currNode.getTextContent());
        }

        var list = currNode.getChildNodes();
        var objMap = new KasperMap();
        for (int i=0; i<list.getLength(); i+=2){
            var keyNode = list.item(i+1);
            var valueNode = keyNode.getChildNodes().item(0);
            var keyName = list.item(i);
            objMap.put(keyName.getTextContent(), recursivelyConstruct(valueNode));
        } return objMap;
    }
}