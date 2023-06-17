package KasperCommons.Parser;

import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.DataStructures.KasperReference;
import KasperCommons.DataStructures.KasperString;
import KasperCommons.Network.Operations;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KasperDocument {
    /*
    The Kasper Document class helps generate a DOM that defines the query.
     */
    public Document document;
    private DocumentBuilder builder;
    private Element root;
    private Node purpose;
    private Node database;
    private Node args;
    private Node query;

    public Element getRoot() {
        return root;
    }

    public Node getPurpose() {
        return purpose;
    }

    public Node getArgs() {
        return args;
    }

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
        root.appendChild(createNode("exception", ""));
    }

    public void sendOkResponse () {
        addValue(purpose, "response");
        addValue(args, "ok");
        root.appendChild(createNode("exception", ""));
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
    public void setRequest (String path, String key, KasperObject value) {
        addValue(purpose, "set");
        query.appendChild(createNode("path", path));
        args.appendChild(createNode("data_key", key));
        var valueTag = getTag("data");
        valueTag.appendChild(extract(value));
        args.appendChild(valueTag);
        root.appendChild(createNode("exception", ""));
    }

    public void getRequest (String path) {
        addValue(purpose, "get");
        args.appendChild(createNode("path", path));
        root.appendChild(createNode("exception", ""));
    }

    public void response (KasperObject value) {
        var node = extract(value);
        addValue(purpose, "response");
        args.appendChild(node);
        root.appendChild(createNode("exception", ""));
    }

    public void createNode(String path) {
        addValue(purpose, "create node");
        root.appendChild(createNode("path", path));
        root.appendChild(createNode("exception", ""));
    }

    public void createCollection (String path){
        addValue(purpose, "create collection");
        root.appendChild(createNode("path", path));
        root.appendChild(createNode("exception", ""));
    }

    public void doesExist (String path) {
        addValue(purpose, "has");
        root.appendChild(createNode("path", path));
        root.appendChild(createNode("exception", ""));
    }

    public void doesExistResponse (boolean x) {
        if (x) {
            addValue(purpose, "response");
            addValue(args, "yes");
            root.appendChild(createNode("exception", ""));
            return;
        }
        addValue(purpose, "response");
        addValue(args, "no");
        root.appendChild(createNode("exception", ""));
    }

    /*
    This method declares that an exception has been thrown.
     */
    public void raiseException(Exception e){
        var except = getTag("exception");
        var type = createNode("type", e.getClass().getSimpleName());
        var msg = createNode("msg", "\nThrown by KasperEngine: " + e.getMessage());
        except.appendChild(type);
        except.appendChild(msg);
        root.appendChild(except);
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
        if (o instanceof KasperReference) {
            return createNode(o.getType(), o.toStr());
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
            xmlString = null;
            Document document = builder.parse(inputSource);
            inputSource = null;

            // Remove #text nodes from the document
            removeTextNodes(document);

            return new KasperDocument(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void removeTextNodes(Node node) {
        Operations.incrementOperation();
        Operations.incrementOperation();
        NodeList childNodes = node.getChildNodes();
        List<Node> nodesToRemove = new ArrayList<>();

        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.TEXT_NODE) {
                String textContent = childNode.getTextContent().trim();
                if (textContent.isEmpty()) {
                    nodesToRemove.add(childNode);
                }
            } else if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                removeTextNodes(childNode);
            }
        }

        for (Node nodeToRemove : nodesToRemove) {
            node.removeChild(nodeToRemove);
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

    public static void clearDocument(Document document) {
        Element rootElement = document.getDocumentElement();
        removeChildNodes(rootElement);
        document.removeChild(rootElement);
    }

    private static void removeChildNodes(Node node) {
        while (node.hasChildNodes()) {
            Node child = node.getFirstChild();
            node.removeChild(child);
        }
    }




}
