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
        Vector<Byte> bytestream = new Vector<>();
        while (true){
            int x = inputStream.read();
            if (x != 255) bytestream.add((byte)x);
            else break;
        }
        StringBuilder build = new StringBuilder();
        for (byte b : bytestream) {
            build.append((char) b);
        }
        return build.toString();
    }

    public void put (String str) throws IOException {
        outputStream.write(str.getBytes());
        outputStream.write(255);
        outputStream.flush();
    }
}
