package com.kasper.beans.nio.protocol;

import com.kasper.commons.Handlers.CountdownTimer;
import com.kasper.commons.Parser.ByteUtils;
import com.kasper.commons.aliases.Method;
import com.kasper.commons.datastructures.JSONUtils;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.exceptions.BeanConcurrencyException;
import com.kasper.commons.exceptions.KasperException;
import com.kasper.commons.exceptions.KasperTimeoutException;
import io.netty.util.CharsetUtil;

import java.io.*;
import java.net.Socket;

public class Wire {
    private Socket socket;
    private BufferedInputStream reader;
    private BufferedOutputStream writer;
    private long threadID;

    public Wire (Socket socket, long threadID) {
        this.threadID = threadID;
        try {
            writer = new BufferedOutputStream(socket.getOutputStream());
            reader = new BufferedInputStream(socket.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void ensureSynchronized () {
        if (Thread.currentThread().getId() != threadID) throw new BeanConcurrencyException();
    }

    public synchronized String write (int method, String query) {
        assert (method < 100) : "Invalid method. Please with check your driver provider.";
        ensureSynchronized();
        try {
            var byteStream = query.getBytes(CharsetUtil.UTF_16);
            writer.write(method);
            var writeArray = ByteUtils.intToBytes(byteStream.length);
            writer.write(writeArray);
            writer.write(byteStream);
            writer.flush();

            // Now get the result set
            int methodResult = reader.read();
            byte[] length = new byte[4];

            // assert that length == 4
            CountdownTimer timer = new CountdownTimer(15000, ()->{
                throw new KasperTimeoutException();
            });
            timer.start();
            while (socket.getInputStream().available() < 4) {}
            timer.stop();
            length = reader.readNBytes(4);
            int primitiveLength = ByteUtils.bytesToInt(length);
            byte[] result = new byte[primitiveLength];
            for (int i=0; i<primitiveLength; i++) {
                result[i] = nextByte();
            }
            return new String(result, CharsetUtil.UTF_16);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public synchronized byte nextByte() throws IOException {
        CountdownTimer timer = new CountdownTimer(15000, ()->{
            throw new KasperTimeoutException();
        });
        timer.start();
        while (socket.getInputStream().available() < 1) {}
        timer.stop();
        return (byte) reader.read();
    }

    public synchronized void close () {
        try {
            writer.close();
            reader.close();
            socket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void authorization (String username, String password) {
        ensureSynchronized();
        try {
            writer.write(Method.AUTH);
            KasperMap map = new KasperMap();
            map.put("username", username);
            map.put("password", password);
            var objStr = JSONUtils.objectToJsonStream(map);
            var result = write(Method.AUTH, objStr);
            var resultMap = JSONUtils.parseJson(result).toMap();
            if (resultMap.get("exception")==null) return;
            throw new KasperException(resultMap.get("exception").toString());
        } catch (Exception e) {
            throw new KasperException(e);
        }

    }




}
