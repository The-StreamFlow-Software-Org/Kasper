package KasperCommons.Network;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class NetworkPackage {
    public BufferedInputStream inputStream;
    public BufferedOutputStream outputStream;
    public Socket socket;
    public InputStream in;
    public OutputStream out;


    public NetworkPackage (Socket socket) throws IOException {
        this.socket = socket;
        socket.setTcpNoDelay(true);
        out = socket.getOutputStream();
        in = socket.getInputStream();
        outputStream = new BufferedOutputStream(out);
        inputStream = new BufferedInputStream(in);
    }

    public byte[] get () throws IOException {
        Timer t = new Timer();
        t.start();
        byte[] lengthBytes = new byte[4];
        for (int i=0; i<4; i++){
            lengthBytes[i] = (byte)inputStream.read();
        }
        var length = byteArraytoInt(lengthBytes);
        byte[] stream = new byte[length];
        for (int i=0 ; i<length; i++) stream[i] = (byte)inputStream.read();
       // System.out.println("Overhead for get socket = in seconds: " + t.stop());
        return stream;
    }

    private byte[] intToByteArray (int int32) {
        return ByteBuffer.allocate(4).putInt(int32).array();
    }

    private int byteArraytoInt (byte[] byteArray) {
       return ByteBuffer.wrap(byteArray).getInt();
    }

    public void put (byte[] stream) throws IOException {
        outputStream.write(intToByteArray(stream.length));
        outputStream.write(stream);
        outputStream.flush();
    }

    public String getString () throws IOException {
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
        outputStream.write(str.getBytes());
        outputStream.write(255);
        outputStream.flush();
        // @debug
    }
}
