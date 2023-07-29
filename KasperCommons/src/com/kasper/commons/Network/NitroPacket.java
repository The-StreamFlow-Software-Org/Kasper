package com.kasper.commons.Network;

import com.kasper.commons.Handlers.CountdownTimer;
import com.kasper.commons.Parser.ExceptionAlias;
import com.kasper.commons.aliases.CommandAlias;
import com.kasper.commons.aliases.Method;
import com.kasper.commons.authenticator.KasperCommons.Authenticator.PacketOuterClass;
import com.kasper.commons.authenticator.KasperCommons.Authenticator.PreparedPacket;
import com.kasper.commons.exceptions.KasperException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class NitroPacket implements AutoCloseable {
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;
    private Socket socket;

    public Socket socket() {
        return socket;
    }

    private static long defaultTimeout = 5000;

    public NitroPacket(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new BufferedInputStream(socket.getInputStream());
        this.outputStream = new BufferedOutputStream(socket.getOutputStream());
    }

    public void write(byte[] packetBytes, int method) {
        if (method == Method.QUERY || method == Method.RESPONSE_QUERY) {
            try {
                // get the packetByte size first, turn into byte array
                var packetSize = intToByteArray(packetBytes.length);
                for (var x : packetSize) {
                    outputStream.write(x);
                }
                outputStream.write(method);
                for (var x : packetBytes) {
                    outputStream.write(x);
                }
                outputStream.flush();
            } catch (IOException e) {
                throw new KasperException("Kasper:> Cannot write to socket.");
            }
        } else {
            throw new KasperException("Kasper:> Method not supported.");
        }
    }

    // create a method that turns an int into a byte array of size 4
    private byte[] intToByteArray(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    // create a method that turns a byte array of size 4 into an int
    private int byteArrayToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public byte[] read() throws IOException {
        byte[] lengthBytes = new byte[4];
        for (int i=0; i<4; i++){
            lengthBytes[i] = nextByte();
        }
        nextByte();
        int length = byteArrayToInt(lengthBytes);
        byte[] packetBytes = new byte[length];
        for (int i=0; i<length; i++){
            packetBytes[i] = nextByte();
        }
        return packetBytes;
    }

    public byte nextByte() {
        try {
            CountdownTimer timer = new CountdownTimer(defaultTimeout, ()->{
                PreparedPacket packet = new PreparedPacket();
                packet.setHeader(CommandAlias.RESPONSE_ERROR);
                packet.setException(ExceptionAlias.KASPER_EXCEPTION);
                packet.setExceptionMsg("Request timed out at concurrent connection levels: " + KasperNitroWire.operations);
                write(packet.build().toByteArray(), Method.QUERY);
                throw new KasperException("Request timed out.");
            });
            timer.start();
            while (socket.isConnected() && inputStream.available() == 0) {
                if (socket.isClosed()) throw new KasperException("Socket is closed.");
            }
            timer.stop();
            return (byte) inputStream.read();
        } catch (IOException e) {
            throw new KasperException(e);
        }
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }
}
