package Parser;

import org.jetbrains.annotations.Contract;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 The Manifest class contains and reads data from the manifest file.
 This contains metadata about the driver and the server.
 */
public class Manifest {

    private static BufferedReader reader = null;
    private static String serial = null;


    private static void init(){
        try {
            if (reader == null) {
                reader = new BufferedReader(new FileReader("KasperCommons/src/Parser/kasper.manifest"));
                serial = extractArgs(reader.readLine());
            }
        } catch (IOException e){
            throw new RuntimeException("Kasper Manifest file is missing.");
        }
    }

    public static String getSerial(){
        init();
        return serial;
    }

    @Contract(pure = true)
    private static String extractArgs(String s){
        return s.split("=")[1];
    }
}
