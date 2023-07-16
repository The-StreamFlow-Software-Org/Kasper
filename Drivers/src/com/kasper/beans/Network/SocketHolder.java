package com.kasper.beans.Network;

import com.kasper.commons.Network.KasperNitroWire;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Stack;

public class SocketHolder {
    public KasperNitroWire[] packages;

    protected SocketHolder (){
        packages = new KasperNitroWire[5];
    }

    public static SocketHolder instantiate(ArrayList<String> sockets, String host) throws IOException {
        var pack = new SocketHolder();
        for (int i=0; i<5; i++){
            var socket = new Socket(host, Integer.parseInt(sockets.get(i)));
            pack.packages[i] = new KasperNitroWire(socket);
        } return pack;
    }

    protected int recorded;
    protected Stack<Integer> streamBytes;

    public void setStream(int bytes) {
        recorded=bytes;
        streamBytes = new Stack<>();
        for (int i=0; i<5; i++){
            streamBytes.push(bytes/6);
            recorded-=bytes/6;
        }streamBytes.push(recorded);
    }

    protected int getNext(){
        return streamBytes.pop();
    }

}
