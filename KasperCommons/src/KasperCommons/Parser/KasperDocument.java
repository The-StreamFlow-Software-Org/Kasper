package KasperCommons.Parser;

import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.DataStructures.KasperString;
import KasperCommons.Exceptions.KasperException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.Map;

public class KasperDocument {
    /*
    The Kasper Document class helps generate a DOM that defines the query.
     */
    Document document;
    private DocumentBuilder builder;
    private Element root;
    private Node purpose;
    private Node database;
    private Node args;
    private Node query;


    KasperDocument (DocumentBuilder builder){
        this.builder = builder;
        document = builder.newDocument();
        this.root = document.createElement("kasper");
        root.appendChild(createNode("serial", Manifest.getSerial(new KasperAccessAuthenticator("kasper.util.key"))));
        query = getTag("query");
        document.appendChild(root);
        root.appendChild(query);
        purpose = getTag("for");
        query.appendChild(purpose);
        args = getTag("args");
        query.appendChild(args);
    }

    public KasperDocument (Document document) throws ParserConfigurationException {
        builder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
        root = (Element)document.getChildNodes().item(0);
        this.document = document;
    }

    public Document getDocument (KasperAccessAuthenticator auth) {
        return document;
    }

    /*
    Generates a custom tag.
     */
    public Node getTag (String tagName){
        return document.createElement(tagName);
    }


    /**
     *
     * @return returns the query node for nesting queries in one statement
     */
    public Node getQuery() {
        return query;
    }

    public void addQuery (Node query){
        var clone = query.cloneNode(true);
        document.adoptNode(clone);
        root.appendChild(clone);
    }


    // main interactions

    /*
    This method is for sending auth requests.
     */
    public void authRequest(String username, String password){
        addValue(purpose, "auth");
        args.appendChild(createNode("user", username));
        args.appendChild(createNode("password", password));
    }


    /*
    Used in server components.
     */
    public void addFor (String type){
        addValue(purpose, type);
    }

    public void addArgs (Node n){
        args.appendChild(n);
    }

    /*
    This method is for set requests.
     */
    public void setRequest (String key, KasperObject value) {
        addValue(purpose, "set");
        args.appendChild(createNode("collection_key", key));
        var valueTag = getTag("collection_value");
        valueTag.appendChild(extract(value));
        args.appendChild(valueTag);
    }

    /*
    This method declares that an exception has been thrown.
     */
    public void raiseException(KasperException e){
        var except = getTag("exception");
        addValue(except, e.getClass().getSimpleName());
    }


    /*
    Appends a node to the root.
     */
    protected void appendToTree(Node n) {
        root.appendChild(n);
    }

    /*
    Adds a string value to an existing node.
     */
    protected void addValue (Node n, String s){
        n.appendChild(document.createTextNode(s));
    }


    /*
    Creates a tag with a specified text value
     */
    public Node createNode(String tagName, String content){
        var element = getTag(tagName);
        element.appendChild(document.createTextNode(content));
        return element;
    }


    /*
    Creates an XML node that extracts the data
    out of a KasperObject instance.
     */
    public Node extract (KasperObject o){
        return recursive_extraction(o);
    }

    private Node recursive_extraction (KasperObject o){
        if (o instanceof KasperString) {
            return createNode(o.getType(), o.toStr());
        }
        if (o instanceof KasperList) {
            var holder = getTag(o.getType());
            for (var elem : o.toList()) {
                holder.appendChild(recursive_extraction(elem));
            } return holder;
        }
        var holder = getTag(o.getType());
        for (Object elem : o.toMap().entrySet()) {
            var kvp = (Map.Entry)elem;
            var key = getTag("key");
            key.appendChild(document.createTextNode(((Map.Entry<String, KasperObject>) elem).getKey()));
            var value = getTag("value");
            value.appendChild(recursive_extraction((KasperObject) kvp.getValue()));
            holder.appendChild(key);
            holder.appendChild(value);
        } return holder;
    }

    /*
    Stringifies a given node.
     */
    public String nodeToString(Node node) {
        try {
            // Create a new document for serialization
            Document doc = builder.newDocument();

            // Import the node into the new document
            Node importedNode = doc.importNode(node, true);
            doc.appendChild(importedNode);

            // Serialize the new document to string
            DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
            LSSerializer serializer = domImplementation.createLSSerializer();
            serializer.getDomConfig().setParameter("format-pretty-print", true);
            LSOutput output = domImplementation.createLSOutput();
            output.setEncoding("UTF-8");

            // Create a writer to capture the serialized output
            StringWriter writer = new StringWriter();
            output.setCharacterStream(writer);
            serializer.write(doc, output);

            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
    Sets context
     */
    protected void setContext(){

    }

    /*
    Stringifies this document.
     */
    @Override
    public String toString() {
        return nodeToString(root);
    }

    public static KasperDocument constructor(String xmlString) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xmlString));
            Document document = builder.parse(inputSource);

            // Remove #text nodes from the document
            removeTextNodes(document);

            return new KasperDocument(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void removeTextNodes(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i = childNodes.getLength() - 1; i >= 0; i--) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.TEXT_NODE) {
                String textContent = childNode.getTextContent().trim();
                if (textContent.isEmpty()) {
                    node.removeChild(childNode);
                }
            } else if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                removeTextNodes(childNode);
            }
        }
    }

    public void clear() {
        Element root = document.getDocumentElement();
        clearElement(root);
    }

    private void clearElement(Element element) {
        NodeList childNodes = element.getChildNodes();
        int length = childNodes.getLength();

        for (int i = length - 1; i >= 0; i--) {
            Node child = childNodes.item(i);
            element.removeChild(child);
        }
    }



}
