package com.kasper.beans.nio.protocol;

import com.kasper.beans.nio.streamflow.ResultSet;
import com.kasper.commons.Parser.ByteUtils;
import com.kasper.commons.aliases.Method;
import com.kasper.commons.datastructures.JSONUtils;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.exceptions.BeanConcurrencyException;
import com.kasper.commons.exceptions.KasperException;
import com.kasper.commons.exceptions.StreamFlowException;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Wire {
    private SocketChannel socketChannel;
    private ByteBuffer byteBuffer;
    private long threadID;

    public Wire (InetSocketAddress socketAddress, long threadID) {
        this.threadID = threadID;
        this.byteBuffer = ByteBuffer.allocate(1024); // can be adjusted based on your need
        try {
            this.socketChannel = SocketChannel.open(socketAddress);
            this.socketChannel.configureBlocking(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void ensureSynchronized () {
        if (Thread.currentThread().getId() != threadID) throw new BeanConcurrencyException();
    }

    public String write (int method, String query) {
        assert (method < 100) : "Invalid method. Please with check your driver provider.";
        ensureSynchronized();
        try {
            var byteStream = query.getBytes(StandardCharsets.UTF_16);
            byteBuffer.clear();
            byteBuffer.put((byte) method);
            byteBuffer.putInt(byteStream.length);
            byteBuffer.put(byteStream);
            byteBuffer.flip();

            while(byteBuffer.hasRemaining()) {
                socketChannel.write(byteBuffer);
            }

            // Now get the result set
            byteBuffer.clear();
            while (socketChannel.read(byteBuffer) < 5) { // Changed from 1 to 5
                // you might want to implement a timeout mechanism here.
            }

            byteBuffer.flip();
            int methodResult = byteBuffer.get();
            byte[] lengthBytes = new byte[4];
            byteBuffer.get(lengthBytes, 0, 4);
            int length = ByteUtils.bytesToInt(lengthBytes);
            byte[] result = new byte[length];

            // Start staging bytes in batches of four
            int byteBatch = 4;
            int batches = length / byteBatch;
            int remainder = length % byteBatch;
            for (int i = 0; i < batches; i++) {
                while (byteBuffer.remaining() < byteBatch) {
                    byteBuffer.compact();
                    socketChannel.read(byteBuffer);
                    byteBuffer.flip();
                }
                byteBuffer.get(result, i*byteBatch, byteBatch);
            }

            // Handling the case when length is not divisible by 4
            if (remainder != 0) {
                while (byteBuffer.remaining() < remainder) {
                    byteBuffer.compact();
                    socketChannel.read(byteBuffer);
                    byteBuffer.flip();
                }
                byteBuffer.get(result, batches*byteBatch, remainder);
            }

            return new String(result, StandardCharsets.UTF_16);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void close () throws StreamFlowException {
        try {
            socketChannel.close();
        } catch (Exception e) {
            throw new StreamFlowException(e);
        }
    }

    public void authorization (String username, String password) throws StreamFlowException {
        ensureSynchronized();
        try {
            KasperMap map = new KasperMap();
            map.put("username", username);
            map.put("password", password);
            var objStr = JSONUtils.objectToJsonStream(map);
            var result = write(Method.AUTH, objStr);
            ResultSet set = new ResultSet(result);
            set.getNext();
        } catch (StreamFlowException x) {
            throw x;
        }  catch (Exception e) {
            throw new KasperException(e);
        }
    }
}
