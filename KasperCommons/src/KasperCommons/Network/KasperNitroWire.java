package KasperCommons.Network;

import KasperCommons.Concurrent.Pool;

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

    public void close () throws IOException {
        if (nitroSocket != null) nitroSocket.close();
        if (outputStream != null) outputStream.close();
        if (inputStream != null) inputStream.close();
        if (in != null) in.close();
        if (out != null) out.close();
        if (nitroIn != null) nitroIn.close();
        if (nitroOut != null) nitroOut.close();
        if (socket != null) socket.close();
    }


    public KasperNitroWire(Socket socket, Boolean flag) throws IOException {
        this.socket = socket;
        socket.setTcpNoDelay(true);
        out = socket.getOutputStream();
        in = socket.getInputStream();
        outputStream = new BufferedOutputStream(out);
        inputStream = new BufferedInputStream(in);
        nitroSocket = new Socket(socket.getInetAddress(), socket.getPort()+1);
        nitroIn = new BufferedInputStream(nitroSocket.getInputStream());
        nitroOut = new BufferedOutputStream(nitroSocket.getOutputStream());
    }

    public KasperNitroWire(Socket socket) throws IOException {
        this.socket = socket;
        socket.setTcpNoDelay(true);
        out = socket.getOutputStream();
        in = socket.getInputStream();
        outputStream = new BufferedOutputStream(out);
        inputStream = new BufferedInputStream(in);
    }

    public void setNitro(Socket socket) throws IOException {
        nitroIn = new BufferedInputStream(socket.getInputStream());
        nitroOut = new BufferedOutputStream(socket.getOutputStream());
    }







    public byte[] get() throws IOException, InterruptedException {
        int numSock = inputStream.read();
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
        return ByteBuffer.allocate(4).putInt(int32).array();
    }

    private int byteArraytoInt (byte[] byteArray) {
       return ByteBuffer.wrap(byteArray).getInt();
    }

    public void put (byte[] stream) throws IOException {
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
        if (totalBytes < 500000) {
            result[0] = 1;
        } else {
            result[0] = 2; // change to enable
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
