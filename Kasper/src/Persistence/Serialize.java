package Persistence;

import java.io.*;
import java.util.ArrayList;

public class Serialize {
    private FileOutputStream fileOut;
    private ObjectOutputStream out;
    private FileInputStream fileIn;
    private final String extension = ".ser";

    /*
    Java Helper Class For Serialization, (c) Offline QuizQuestions.Quiz, 2023
    author: Rufelle Emmanuel Pactol
     */

    private ObjectInput in;


    /**
     *
     * @param output Specifies the output file.
     * @param isReadMode Set as 'true' if we are reading to a serialized file or writing
     *                   to a present database.
     *                    Setting to 'false' will delete all object in the file.
     * @throws IOException Bubbled exception from Serializable interface
     */
    public Serialize (String output, boolean isReadMode) throws IOException {
        fileOut = new FileOutputStream(output + extension, isReadMode);
        out = new ObjectOutputStream(fileOut);
        fileIn = new FileInputStream(output + extension);
        in = new ObjectInputStream(fileIn);
    }


    /**
     * @brief Serializes Java Object into a bytestream.
     * @param o
     * @return
     * @throws IOException
     */
    public static <Typename> byte[] writeToBytes (Typename o) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(o);
        return out.toByteArray();
    }

    /**
     * Constructs a Java Object from a blob.
     */
    public static <Typename> Typename constructFromBlob (InputStream blob) throws IOException, ClassNotFoundException {
        ObjectInputStream input = new ObjectInputStream(blob);
        return (Typename) input.readObject();
    }

    public static <Typename> Typename constructFromBlob (byte[] blob) throws IOException, ClassNotFoundException {
        System.out.println("Kasper:> Reconstructing persistence data. Actual uncompressed data size is " + (blob.length / 1000000.00) + " megabytes.");
        try (ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(blob))){
            return (Typename) input.readObject();
        }
    }

    /**
     *
     * @param object Serializes this object.
     */
    public <T extends Serializable> void serialize(T object ) throws IOException {
        out.writeObject(object);
    }


    /**
     *
     * @return Returns the object from the specified .ser file.
     */
    public <T> T getObject() throws IOException, ClassNotFoundException {
        return (T)in.readObject();
    }


    /**
     * @brief Closes all IO members.
     * @throws IOException
     */
    void close() throws IOException {
        out.close();
        fileOut.close();
        in.close();
        fileIn.close();
    }

    <T> ArrayList<T> getAllObjects () {
        ArrayList<T> objects = new ArrayList<>();

        try{
            while (true){
                objects.add(getObject());
            }
        } catch (Exception ignored){}
        return objects;
    }


}


