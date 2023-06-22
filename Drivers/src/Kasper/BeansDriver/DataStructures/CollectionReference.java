package Kasper.BeansDriver.DataStructures;

import KasperCommons.Annotations.RawKasperReferenceUsage;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.Authenticator.KasperCommons.Authenticator.PacketOuterClass;
import KasperCommons.Authenticator.KasperCommons.Authenticator.PreparedPacket;
import KasperCommons.DataStructures.*;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Exceptions.KasperIOException;
import KasperCommons.Exceptions.NoSuchKasperObject;
import KasperCommons.Handlers.ExceptionHandler;
import KasperCommons.Network.NetworkPackage;
import KasperCommons.Parser.KasperDocument;
import KasperCommons.Parser.KasperWriter;
import KasperCommons.Parser.PathParser;
import KasperCommons.Parser.TokenSender;
import com.sun.source.tree.PackageTree;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class CollectionReference extends AbstractReference{
    protected NodeReference parent;
    protected String nodeName;

    protected CollectionReference(String name, NodeReference parent) {
        super("collection");
        this.parent = parent;
        password = parent.password;
        host = parent.host;
        user = parent.host;
        nodeName = parent.name;
        this.name = name;
        this.networkPackage = parent.networkPackage;
    }

    /**
     * @brief Attempts to retrieve an object from the collection.
     * @param keyName the key to retrieve.
     * @return a KasperObject wrapped in the Optional class.
     */
    public Optional<KasperObject> tryGetKey(String keyName){
        return tryGetKey(generateReference(keyName));
    }

    /**
     *
     * @brief Attempts to retrieve an object from the collection.
     * @param keyName the path of the object to retrieve.
     * @return a KasperObject wrapped in the Optional class.
     */
    public Optional<KasperObject> tryGetKey(KasperReference keyName){
        Optional<KasperObject> optional = Optional.empty();
        try {
            optional = Optional.of(getKey(keyName));
        } catch (NoSuchKasperObject noInstance) {}
        return optional;
    }

    /**
     * @brief Retrieves an object from the collection.
     * @param keyName the key of the object to retrieve.
     * @return a KasperObject wrapped in the Optional class.
     * @throws NoSuchKasperObject when the key is not found in the database.
     */
    public KasperObject getKey(String keyName){
        return getKey(generateReference(keyName));
    }

    /**
     * @brief Retrieves an object from the collection.
     * @param reference the path of the object to retrieve.
     * @return a KasperObject wrapped in the Optional class.
     * @throws NoSuchKasperObject when the key is not found in the database.
     */
    public KasperObject getKey(KasperReference reference){
        try{
            PreparedPacket packet =  new PreparedPacket();
            packet.setHeader(2);
            packet.addArg("path", reference.toStr());
            networkPackage.put(packet.build().toByteArray());
            var resultant = PacketOuterClass.Packet.parseFrom(networkPackage.get());
            TokenSender.resolveExceptions(resultant);
            var data = JSONUtils.parseJson(resultant.getData());

            // initialize path crawling
            LocalPathCrawler.finalPathSetter(data, resultant.getArgsMap().get("path"));
            LocalPathCrawler.crawlPaths(data);
            return data;
        } catch (Exception e) {
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
    }

    /**
     * @brief Inserts an object to the collection.
     * @param referencePath the path to where the object is inserted. (Path of the object's parent)
     * @param key [map] for objects inserted to maps, this specifies the key of the object upon insertion. <br>[list] for objects inserted to lists, this specifies the index of the object upon insertion. Can be 'head' or 'tail' to insert the object as the first or last object respectively. Or can be numeric to specify the index. <br>[string] this parameter is ignored for strings.
     * @param value is the object to be inserted
     * @throws KasperCommons.Exceptions.KasperObjectAlreadyExists when the object is inserted to a map where the key already exists.
     */
    public void setKey(KasperReference referencePath, String key, KasperObject value) {
        try {
            if (key.charAt(0) == '$') throw new KasperException("Thrown by KasperDriver.\nReason:> Keys cannot start with reserved character '$'.");
            PreparedPacket packet = new PreparedPacket();
            packet.setHeader(1);
            packet.setData(value);
            packet.addArg("path", referencePath.toStr());
            packet.addArg("key", key);
            networkPackage.put(packet.build().toByteArray());
            TokenSender.resolveExceptions(
                    PacketOuterClass.Packet
                            .parseFrom(networkPackage.get()));
        } catch (Exception e){
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
    }

    /**
     * @brief Inserts an object to the collection.
     * @param referencePath the path to where the object is inserted. (Path of the object's parent)
     * @param key [map] for objects inserted to maps, this specifies the key of the object upon insertion. <br>[list] for objects inserted to lists, this specifies the index of the object upon insertion. Can be 'head' or 'tail' to insert the object as the first or last object respectively. Or can be numeric to specify the index. <br>[string] this parameter is ignored for strings.
     * @param value the string to be inserted
     * @throws KasperCommons.Exceptions.KasperObjectAlreadyExists when the object is inserted to a map where the key already exists.
     */
    public void setKey (KasperReference referencePath, String key, String value) throws KasperException{
        setKey(referencePath, key, KasperObject.str(value));
    }

    /**
     *
     * @brief Inserts an object to the collection.
     * @param referencePath the raw path to where the object is inserted. (Path of the object's parent)
     * @param key [map] for objects inserted to maps, this specifies the key of the object upon insertion. <br>[list] for objects inserted to lists, this specifies the index of the object upon insertion. Can be 'head' or 'tail' to insert the object as the first or last object respectively. Or can be numeric to specify the index. <br>[string] this parameter is ignored for strings.
     * @param value is the object to be inserted
     * @throws KasperCommons.Exceptions.KasperObjectAlreadyExists when the object is inserted to a map where the key already exists.
     */
    @RawKasperReferenceUsage
    public void setKey (String referencePath, String key, String value) throws KasperException{
        setKey(generateReference(referencePath), key, KasperObject.str(value));
    }

    /**
     *
     * @brief Inserts an object to the collection.
     * @param referencePath the raw path to where the object is inserted. (Path of the object's parent)
     * @param key [map] for objects inserted to maps, this specifies the key of the object upon insertion. <br>[list] for objects inserted to lists, this specifies the index of the object upon insertion. Can be 'head' or 'tail' to insert the object as the first or last object respectively. Or can be numeric to specify the index. <br>[string] this parameter is ignored for strings.
     * @param value is the object to be inserted
     * @throws KasperCommons.Exceptions.KasperObjectAlreadyExists when the object is inserted to a map where the key already exists.
     */
    @RawKasperReferenceUsage
    public void setKey (String referencePath, String key, KasperObject value) throws KasperException{
        setKey(generateReference(referencePath), key, value);
    }


    /**
     * @brief Inserts a string to the collection.
     * @param key The key for the object to be inserted.
     * @param value The string to be inserted to the collection.
     * @throws KasperCommons.Exceptions.KasperObjectAlreadyExists when the object's key is already inside the database.
     */
    public void setKey (String key, String value) throws KasperException {
        setKey(key, KasperObject.str(value));
    }

    /**
     * @brief Inserts an object to the collection.
     * @param key The key for the object to be inserted.
     * @param value The object to be inserted to the collection.
     * @throws KasperCommons.Exceptions.KasperObjectAlreadyExists when the object's key is already inside the database.
     */
    public void setKey(String key, KasperObject value) {
        setKey(generateReference(), key, value);
    }

    /**
     * @brief generates safe KasperReference with this collection as a base. The args will be appended to the current collection's path. <br>(i.e., args ["this", "path"] output reference: ["node.collection.this.path"]
     * @param path the path specifying the reference. Leftmost argument is the highest in the object hierarchy.
     * @return a KasperReference with parsed references
     */
    public KasperReference generateReference (String ... path) {
        PathParser parser = new PathParser();
        for (var x : path) {
            parser.addPathConventionally(x);
        }
        parser.addPath(name);
        parser.addPath(parent.name);
        return new KasperReference(parser.parsePath());
    }

    /**
     * @brief generates an unsafe raw reference to a path.
     * @param rawPath the path, delimited by '.' per key.
     * @return a new KasperReference with the raw path inside.
     */
    @RawKasperReferenceUsage
    public KasperReference rawReference (String rawPath) {
        PathParser parser = new PathParser();
        parser.addPath(name);
        parser.addPath(parent.name);
        return new KasperReference(parser.parsePath() +  "." + rawPath);
    }

    /**
     * @brief a general method for searching
     * @param path contains the path to search.
     * @return a KasperFindQuery that will finish the transaction.
     */
    public KasperFindQuery in (KasperReference path){
        return new KasperFindQuery(path, networkPackage);
    }

    /**
     * @brief a general method for searching
     * @param path contains the path to search. Leave blank if the search target is this collection.
     * @return a KasperFindQuery that will finish the transaction.
     */
    @RawKasperReferenceUsage
    public KasperFindQuery in (String path) {
        var ref = new KasperReference("");
     //   ref.getPath() = path;
        return new KasperFindQuery(ref, networkPackage);
    }

    public class KasperFindQuery {
        protected KasperDocument document;
        protected NetworkPackage pack;
        protected KasperReference path;
        protected KasperFindQuery (KasperReference path, NetworkPackage pack) {
            this.document = KasperWriter.newDocument(KasperAccessAuthenticator.getKey());
            this.pack = pack;
            this.path= path;
        }

        /**
         * @brief searches the path passed in the [in] method for all objects that have fields specified in the rawPath.
         * @param properties specifies the properties match. For example, in the path "node.collection.person", when the properties are specified as "favorites.iceCream", it will iterate through all the objects in "node.collection.person" looking for  all objects with a "favorites.iceCream" property.
         * @return a KasperList containing all the matching objects.
         */
        @RawKasperReferenceUsage
        public KasperList hasRawProperties (String properties){
            try {
                var basePath = this.path.toStr();
                StringBuilder build = new StringBuilder();
                build.append(basePath).append(".").append(properties);
                document.containsRequest(basePath, build.toString());
                pack.put(document.toString());
              //  return new KasperConstructor(Objects.requireNonNull(KasperDocument.constructor(pack.get()))).constructObject().castToList();
            } catch (IOException e) {
                throw new KasperException(e.getMessage());
            } return null;
        }

        /**
         * @brief searches the path passed in the [in] method for all objects that have fields specified in the rawPath.
         * @param properties specifies the properties match. For example, in the path "node.collection.person", when the properties are specified as "favorites.iceCream", it will iterate through all the objects in "node.collection.person" looking for  all objects with a "favorites.iceCream" property.
         * @return a KasperList containing all the matching objects.
         */
        public KasperList hasProperties (String ... properties)  {
            try {
                var basePath = this.path.toStr();
                for (var x : properties) {
                    this.path.addPathConventionally(x);
                }
                document.containsRequest(basePath, this.path.toStr());
                pack.put(document.toString());
          //      return new KasperConstructor(Objects.requireNonNull(KasperDocument.constructor(pack.get()))).constructObject().castToList();
            } catch (IOException e) {
                throw new KasperException(e.getMessage());
            } return null;
        }


    }

    /**
     * @brief clears all the global data, including nodes.
     */
    public void clear () {
        try {
            networkPackage.put(TokenSender.clear().toByteArray());
            TokenSender.resolveExceptions(PacketOuterClass.Packet.parseFrom(networkPackage.get()));
        }  catch (Exception e){
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
    }







}
