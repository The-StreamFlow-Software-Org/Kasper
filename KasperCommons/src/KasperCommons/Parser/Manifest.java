package KasperCommons.Parser;

import KasperCommons.Authenticator.KasperAccessAuthenticator;
import com.sun.tools.javac.Main;
import org.jetbrains.annotations.Contract;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 The Manifest class contains and reads data from the manifest file.
 This contains metadata about the driver and the server.
 */
class Manifest {

    private static BufferedReader reader = null;
    private static String serial = null;


    private static void init(){
        if (reader == null) {
            serial = "Alfalfa.0.1";
        }
    }

    private Manifest (){}

    public static String getSerial(KasperAccessAuthenticator auth){
        init();
        return serial;
    }

    @Contract(pure = true)
    private static String extractArgs(String s){
        return s.split("=")[1];
    }
}
