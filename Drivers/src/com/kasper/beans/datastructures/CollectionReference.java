package com.kasper.beans.datastructures;

import com.kasper.commons.aliases.CommandAlias;
import com.kasper.commons.annotations.RawKasperReferenceUsage;
import com.kasper.commons.authenticator.KasperAccessAuthenticator;
import com.kasper.commons.authenticator.KasperCommons.Authenticator.PacketOuterClass;
import com.kasper.commons.authenticator.KasperCommons.Authenticator.PreparedPacket;
import com.kasper.commons.datastructures.*;
import com.kasper.commons.exceptions.ExperimentalFeatureException;
import com.kasper.commons.exceptions.KasperException;
import com.kasper.commons.exceptions.NoSuchKasperObject;
import com.kasper.commons.Network.KasperNitroWire;
import com.kasper.commons.Parser.KasperDocument;
import com.kasper.commons.Parser.KasperWriter;
import com.kasper.commons.Parser.PathParser;
import com.kasper.commons.Parser.TokenSender;
import com.kasper.commons.exceptions.KasperObjectAlreadyExists;

import java.io.IOException;
import java.util.Optional;

import static com.kasper.commons.datastructures.KasperObject.str;

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
        this.kasperNitroWire = parent.kasperNitroWire;
        this.threadID = parent.threadID;
    }



    /**
     * @brief Attempts to retrieve an object from the collection.
     * @param keyName the key to retrieve.
     * @return a KasperObject wrapped in the Optional class.
     */
    public Optional<KasperObject> tryGetKey(String keyName){
        return tryGetKey(generatePathReference(keyName));
    }

    /**
     *
     * @brief Attempts to retrieve an object from the collection.
     * @param keyName the path of the object to retrieve.
     * @return a KasperObject wrapped in the Optional class.
     */
    public Optional<KasperObject> tryGetKey(KasperPathReference keyName){
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
        return getKey(generatePathReference(keyName));
    }

    /**
     * @brief Retrieves an object from the collection.
     * @param path the path of the object to retrieve.
     * @return a KasperObject wrapped in the Optional class.
     * @throws NoSuchKasperObject when the key is not found in the database.
     */
    public KasperObject getKey(KasperPathReference path){
        verifyConcurrency();
        try{
            PreparedPacket packet =  new PreparedPacket();
            packet.setHeader(CommandAlias.GET);
            packet.addArg("path", path.toStr());
            kasperNitroWire.put(packet.build().toByteArray());
            var resultant = PacketOuterClass.Packet.parseFrom(kasperNitroWire.get());
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
     * @param path the path to where the object is inserted. (Path of the object's parent)
     * @param key [map] for objects inserted to maps, this specifies the key of the object upon insertion. <br>[list] for objects inserted to lists, this specifies the index of the object upon insertion. Can be 'head' or 'tail' to insert the object as the first or last object respectively. Or can be numeric to specify the index. <br>[string] this parameter is ignored for strings.
     * @param value is the object to be inserted
     * @throws KasperObjectAlreadyExists when the object is inserted to a map where the key already exists.
     */
    public void setKey(KasperPathReference path, String key, KasperObject value) {
        verifyConcurrency();
        try {
            key = key.intern();
            if (key.charAt(0) == '$') throw new KasperException("Thrown by KasperDriver.\nReason:> Keys cannot start with reserved character '$'.");
            PreparedPacket packet = new PreparedPacket();
            packet.setHeader(CommandAlias.SET);
            packet.setData(value);
            packet.addArg("path", path.toStr());
            packet.addArg("key", key);
            kasperNitroWire.put(packet.build().toByteArray());
            TokenSender.resolveExceptions(
                    PacketOuterClass.Packet
                            .parseFrom(kasperNitroWire.get()));
        } catch (Exception e){
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
    }

    /**
     * @brief Inserts an object to the collection.
     * @param path the path to where the object is inserted. (Path of the object's parent)
     * @param key [map] for objects inserted to maps, this specifies the key of the object upon insertion. <br>[list] for objects inserted to lists, this specifies the index of the object upon insertion. Can be 'head' or 'tail' to insert the object as the first or last object respectively. Or can be numeric to specify the index. <br>[string] this parameter is ignored for strings.
     * @param value the string to be inserted
     * @throws KasperObjectAlreadyExists when the object is inserted to a map where the key already exists.
     */
    public void setKey (KasperPathReference path, String key, String value) throws KasperException{
        setKey(path, key, str(value));
    }

    /**
     *
     * @brief Inserts an object to the collection.
     * @param path the raw path to where the object is inserted. (Path of the object's parent)
     * @param key [map] for objects inserted to maps, this specifies the key of the object upon insertion. <br>[list] for objects inserted to lists, this specifies the index of the object upon insertion. Can be 'head' or 'tail' to insert the object as the first or last object respectively. Or can be numeric to specify the index. <br>[string] this parameter is ignored for strings.
     * @param value is the object to be inserted
     * @throws KasperObjectAlreadyExists when the object is inserted to a map where the key already exists.
     */
    @RawKasperReferenceUsage
    public void setKey (String path, String key, String value) throws KasperException{
        setKey(generatePathReference(path), key, str(value));
    }

    /**
     *
     * @brief Inserts an object to the collection.
     * @param path the raw path to where the object is inserted. (Path of the object's parent)
     * @param key [map] for objects inserted to maps, this specifies the key of the object upon insertion. <br>[list] for objects inserted to lists, this specifies the index of the object upon insertion. Can be 'head' or 'tail' to insert the object as the first or last object respectively. Or can be numeric to specify the index. <br>[string] this parameter is ignored for strings.
     * @param value is the object to be inserted
     * @throws KasperObjectAlreadyExists when the object is inserted to a map where the key already exists.
     */
    @RawKasperReferenceUsage
    public void setKey (String path, String key, KasperObject value) throws KasperException{
        setKey(generatePathReference(path), key, value);
    }


    /**
     * @brief Inserts a string to the collection.
     * @param key The key for the object to be inserted.
     * @param value The string to be inserted to the collection.
     * @throws KasperObjectAlreadyExists when the object's key is already inside the database.
     */
    public void setKey (String key, String value) throws KasperException {
        setKey(key, str(value));
    }

    /**
     * @brief Inserts an object to the collection.
     * @param key The key for the object to be inserted.
     * @param value The object to be inserted to the collection.
     * @throws KasperObjectAlreadyExists when the object's key is already inside the database.
     */
    public void setKey(String key, KasperObject value) {
        setKey(generatePathReference(), key, value);
    }

    /**
     * @brief generates safe KasperReference with this collection as a base. The args will be appended to the current collection's path. <br>(i.e., args ["this", "path"] output path: ["node.collection.this.path"]
     * @param path the path specifying the path. Leftmost argument is the highest in the object hierarchy.
     * @return a KasperReference with parsed paths
     */
    public KasperPathReference generatePathReference (String ... path) {
        verifyConcurrency();
        PathParser parser = new PathParser();
        for (var x : path) {
            parser.addPathConventionally(x);
        }
        parser.addPath(name);
        parser.addPath(parent.name);
        return new KasperPathReference(parser.parsePath());
    }

    /**
     * @brief generates an unsafe raw path to a path.
     * @param rawPath the path, delimited by '.' per key.
     * @return a new KasperReference with the raw path inside.
     */
    @RawKasperReferenceUsage
    public KasperPathReference rawPathReference (String rawPath) {
        verifyConcurrency();
        PathParser parser = new PathParser();
        parser.addPath(name);
        parser.addPath(parent.name);
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(parser.parsePath()).append(".").append(rawPath);
        return new KasperPathReference(pathBuilder.toString());
    }

    /**
     * @brief a general method for searching
     * @param path contains the path to search.
     * @return a KasperFindQuery that will finish the transaction.
     */
    public KasperFindQuery in (KasperPathReference path){
        return new KasperFindQuery(path, kasperNitroWire);
    }

    /**
     * @brief a general method for searching
     * @param path contains the path to search. Leave blank if the search target is this collection.
     * @return a KasperFindQuery that will finish the transaction.
     */
    @RawKasperReferenceUsage
    public KasperFindQuery in (String path) {
        verifyConcurrency();
        var ref = new KasperPathReference("");
     //   ref.getPath() = path;
        return new KasperFindQuery(ref, kasperNitroWire);
    }

    public class KasperFindQuery {
        protected KasperDocument document;
        protected KasperNitroWire pack;
        protected KasperPathReference path;
        protected KasperFindQuery (KasperPathReference path, KasperNitroWire pack) {
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
            verifyConcurrency();
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
            verifyConcurrency();
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
        verifyConcurrency();
        try {
            kasperNitroWire.put(TokenSender.clear().toByteArray());
            TokenSender.resolveExceptions(PacketOuterClass.Packet.parseFrom(kasperNitroWire.get()));
        }  catch (Exception e){
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
    }

    /**
     *
     * @param path Reference to the object to update.
     * @param object Updated data.
     * @warning paths must be re-added.
     */
    public void updateKey(KasperPathReference path, KasperObject object) {
        verifyConcurrency();
        try {
            PreparedPacket packet = new PreparedPacket();
            packet.setHeader(CommandAlias.UPDATE);
            packet.setData(object);
            packet.addArg("path", path.toStr());
            kasperNitroWire.put(packet.build().toByteArray());
            TokenSender.resolveExceptions(PacketOuterClass.Packet.parseFrom(kasperNitroWire.get()));
        } catch (Exception e){
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
    }

    /**
     *
     * @param path Reference to the object to update.
     * @param object Updated data.
     * @warning paths must be re-added.
     */
    public void updateKey(KasperPathReference path, String object) {
        updateKey(path, str(object));
    }

    /**
     *
     * @param path Reference to the object to update.
     * @param object Updated data.
     * @warning paths must be re-added.
     */
    public void updateKey(String path, KasperObject object) {
        updateKey(generatePathReference(path), object);
    }

    /**
     *
     * @param path Reference to the object to update.
     * @param object Updated data.
     * @warning paths must be re-added.
     */
    public void updateKey(String path, String object) {
        updateKey(generatePathReference(path), str(object));
    }

    /**
     * Deletes this collection.
     */
    public void deleteThis () {
        verifyConcurrency();
        if (!EXPERIMENTAL_MODE) throw new ExperimentalFeatureException("deleteThis");
        try {
            PreparedPacket packet = new PreparedPacket();
            packet.setHeader(CommandAlias.DELETE);
            PathParser path = new PathParser();
            path.addPath(parent.name);
            path.addPathConventionally(name);
            packet.addArg("path", path.parsePath());
            kasperNitroWire.put(packet.build().toByteArray());
            TokenSender.resolveExceptions(PacketOuterClass.Packet.parseFrom(kasperNitroWire.get()));
        } catch (Exception e){
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
    }

    /**
     * @param path the path reference of the object to be deleted.
     */
    public void deleteKey (KasperPathReference path) {
        verifyConcurrency();
        try {
        PreparedPacket packet = new PreparedPacket();
        packet.setHeader(CommandAlias.DELETE);
        packet.addArg("path", path.toStr());
        kasperNitroWire.put(packet.build().toByteArray());
        TokenSender.resolveExceptions(PacketOuterClass.Packet.parseFrom(kasperNitroWire.get()));
        } catch (Exception e){
            if (e instanceof KasperException) throw (KasperException)e;
            throw new KasperException(e.getMessage());
        }
    }

    /**
     * @param key the key of the object to be deleted.
     */
    public void deleteKey (String key) {
        deleteKey(generatePathReference(key));
    }






}
