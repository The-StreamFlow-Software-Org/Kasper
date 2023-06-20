package KasperCommons.Network;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class NetworkPackage {
    public BufferedInputStream inputStream;
    public BufferedOutputStream outputStream;
    public Socket socket;
    public InputStream in;
    public OutputStream out;

    public NetworkPackage (Socket socket) throws IOException {
        this.socket = socket;
        out = socket.getOutputStream();
        in = socket.getInputStream();
        outputStream = new BufferedOutputStream(out);
        inputStream = new BufferedInputStream(in);
    }

    public String get () throws IOException {
        Timer t = new Timer();
        t.start();
        StringBuilder build = new StringBuilder();
        while (true){
            int x = inputStream.read();
            if (x != 255) build.append((char)x);
            else break;
        }
        System.out.println("Finished getting over network " + (build.length()/1000.0) + " megabytes in " + t.stop() + "s.");
        return build.toString();
    }

    public void put (String str) throws IOException {
        Timer t = new Timer();
        t.start();
        outputStream.write(str.getBytes());
        outputStream.write(255);
        outputStream.flush();
        // @debug
         System.out.println("Finished sending over network " + (str.length()/1000.0) + " megabytes in " + t.stop() + "s.");
    }
}
