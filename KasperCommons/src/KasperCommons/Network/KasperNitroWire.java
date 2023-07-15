package KasperCommons.Network;

import KasperCommons.Exceptions.KasperException;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

public class KasperNitroWire {
    public BufferedInputStream inputStream;
    public BufferedOutputStream outputStream;
    public Socket socket;
    public Socket nitroSocket;
    public InputStream in;
    public OutputStream out;
    public BufferedInputStream nitroIn;
    public BufferedOutputStream nitroOut;

    public void close ()  {
        try {
            if (nitroSocket != null) nitroSocket.close();
        } catch (IOException ex){}
        try{
            if (outputStream != null) outputStream.close();
        } catch (IOException ex){}
        try{
            if (inputStream != null) inputStream.close();
        } catch (IOException ex){}
        try{
            if (in != null) in.close();
        } catch (IOException ex){}
        try{
            if (out != null) out.close();
        } catch (IOException ex){}
        try{
            if (nitroIn != null) nitroIn.close();
        } catch (IOException ex){}
        try{
            if (nitroOut != null) nitroOut.close();
        } catch (IOException ex){}
        try{
            if (socket != null) socket.close();
        } catch (IOException ex){}

    }


    public KasperNitroWire(Socket socket, Boolean flag) throws IOException {
        this.socket = socket;
        out = socket.getOutputStream();
        in = socket.getInputStream();
        outputStream = new BufferedOutputStream(out);
        inputStream = new BufferedInputStream(in);
        nitroSocket = new Socket(socket.getInetAddress(), socket.getPort()+1);
        nitroIn = new BufferedInputStream(nitroSocket.getInputStream());
        nitroOut = new BufferedOutputStream(nitroSocket.getOutputStream());
    }

    // initializes the default stream
    public KasperNitroWire(Socket socket) throws IOException {
        this.socket = socket;
        socket.setTcpNoDelay(true);
        out = socket.getOutputStream();
        in = socket.getInputStream();
        outputStream = new BufferedOutputStream(out);
        inputStream = new BufferedInputStream(in);
    }

    // initializes the streams for Nitro
    public void setNitro(Socket socket) throws IOException {
        nitroIn = new BufferedInputStream(socket.getInputStream());
        nitroOut = new BufferedOutputStream(socket.getOutputStream());

    }


    public static byte[] encodeIntToBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (value >> 24);
        bytes[1] = (byte) (value >> 16);
        bytes[2] = (byte) (value >> 8);
        bytes[3] = (byte) value;
        return bytes;
    }

    public static int decodeBytesToInt(byte[] bytes) {
        int value = 0;
        value |= (bytes[0] & 0xFF) << 24;
        value |= (bytes[1] & 0xFF) << 16;
        value |= (bytes[2] & 0xFF) << 8;
        value |= bytes[3] & 0xFF;
        return value;
    }



    public byte[] get() throws IOException, InterruptedException {

        // DEBUG
        if (true) {
            byte[] sizeBytes = new byte[4];
            for (int i=0; i<4; i++) {
                sizeBytes[i] = (byte)inputStream.read();
            } int size = decodeBytesToInt(sizeBytes);
            byte[] buffer = new byte[size];
            for (int i=0; i<size; i++) {
                buffer[i] = (byte)inputStream.read();
            } return buffer;
        }




        int numSock =inputStream.read();

        if (numSock == 1) {
            byte[] lengthBytes = new byte[4];

            // Read length
            for (int i = 0; i < 4; i++) {
                lengthBytes[i] = (byte) inputStream.read();
            }

            int length = byteArraytoInt(lengthBytes);
            byte[] stream = new byte[length];

            // Read 'length' bytes from the inputStream and store them in the stream array
            for (int i = 0; i < length; i++) {
                stream[i] = (byte) inputStream.read();
            }

            return stream;
        }

        byte[] lengthBytes1 = new byte[4];
        byte[] lengthBytes2 = new byte[4];

        // Read 4 bytes from the inputStream and store them in lengthBytes1
        for (int i = 0; i < 4; i++) {
            lengthBytes1[i] = (byte) inputStream.read();
        }

        // Read 4 bytes from the nitroIn inputStream and store them in lengthBytes2
        for (int i = 0; i < 4; i++) {
            lengthBytes2[i] = (byte) nitroIn.read();
        }

        int length1 = byteArraytoInt(lengthBytes1);
        int length2 = byteArraytoInt(lengthBytes2);
        byte[] stream = new byte[length2 + length1];

        CountDownLatch latch = new CountDownLatch(1); // Create a CountDownLatch with an initial count of 1

        // Start a new thread to read 'length1' bytes from the inputStream and store them in the stream array
        Thread t = new Thread(() -> {
            try {
                for (int i = 0; i < length1; i++) {
                    stream[i] = (byte) inputStream.read();
                }
                // send stop wait signal here
                latch.countDown(); // Signal that the data loading is complete
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }); t.start();

        for (int i = 0; i < length2; i++) {
            stream[i + length1] = (byte) nitroIn.read();
        }

        // Wait until the latch count reaches zero
        latch.await();

        return stream;
    }


    private byte[] intToByteArray (int int32) {
        var debug =  ByteBuffer.allocate(4).putInt(int32).array();
        for (var x : debug) {
            if (x!=-1) {
                return debug;
            }
        }
        throw new KasperException("Bug found:> Size cannot be negative.\n");
    }

    private int byteArraytoInt (byte[] byteArray) {
        for (var x : byteArray) {
            if (x!=-1) {
                return ByteBuffer.wrap(byteArray).getInt();
            }
        }
        throw new KasperException("Bug found:> Size cannot be negative.\n");
    }

    public void put (byte[] stream) throws IOException {

        if (true) {
            var header = encodeIntToBytes(stream.length);
            System.out.println("PUT: " + stream.length + " decoded: " + decodeBytesToInt(header));
            for (int i =0; i<4; i++){
                outputStream.write(header[i]);
            } for (var x : stream) {
                outputStream.write(x);
            } return;

        }




        var instructor = getChannels(stream.length);
        if (instructor[0] == 1) {
            outputStream.write(instructor[0]);
            outputStream.write(intToByteArray(stream.length));
            outputStream.write(stream);
            outputStream.flush();
            return;
        }
        outputStream.write(instructor[0]);
        int length1 = instructor[1];
        int length2 = instructor[2];

        Thread t = new Thread(()->{
            try {
                nitroOut.write(intToByteArray(length2));
                for (int i=length1; i<length2+length1; i++){
                    nitroOut.write(stream[i]);
                }
                nitroOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }); t.start();

        outputStream.write(intToByteArray(length1));
        for (int i=0; i<length1; i++){
            outputStream.write(stream[i]);
        }
        outputStream.flush();
    }


    protected int[] getChannels(int totalBytes) {
        int[] result = new int[3];
        if (totalBytes < 5000) {
            result[0] = 1;
        } else {
            result[0] = 1; // change to enable
            result[1] = totalBytes/2;
            result[2] = totalBytes - result[1];
        }
        return result;
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
        return build.toString();
    }

    public void put (String str) throws IOException {
        outputStream.write(str.getBytes());
        outputStream.write(255);
        outputStream.flush();
        // @debug
    }
}
