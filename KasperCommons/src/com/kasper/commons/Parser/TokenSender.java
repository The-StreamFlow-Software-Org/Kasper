package com.kasper.commons.Parser;

import com.kasper.commons.aliases.CommandAlias;
import com.kasper.commons.authenticator.KasperCommons.Authenticator.PacketOuterClass;
import com.kasper.commons.authenticator.KasperCommons.Authenticator.PreparedPacket;
import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.exceptions.KasperException;
import com.kasper.commons.exceptions.KasperObjectAlreadyExists;
import com.kasper.commons.exceptions.NoSuchKasperObject;
import com.kasper.commons.exceptions.NotIterableException;

public class TokenSender {


    public static PacketOuterClass.Packet clear () {
        PreparedPacket packet = new PreparedPacket();
        packet.setHeader(16);
        return packet.build();
    }


    public static PacketOuterClass.Packet exist (String path) {
        PreparedPacket packet = new PreparedPacket();
        packet.setHeader(CommandAlias.EXISTS);
        packet.addArg("path", path);
        return packet.build();
    }

    public static PacketOuterClass.Packet get (String path) {
        PreparedPacket packet = new PreparedPacket();
        packet.setHeader(CommandAlias.GET);
        packet.addArg("path", path);
        return packet.build();
    }

    public static PacketOuterClass.Packet responseOK  () {
        PreparedPacket packet = new PreparedPacket();
        packet.setHeader(13);
        return packet.build();
    }

    /**
     Exception data<br>
     0 -> No Exception<br>
     1 -> Exception<br>
     2 -> KasperException<br>
     3 -> KasperObjectAlreadyExistsException<br>
     4 -> NotIterableException<br>
     5 -> NoSuchKasperObjectException<br>
     */
    public static PacketOuterClass.Packet raise (int except, String msg) {
        PreparedPacket packet = new PreparedPacket();
        packet.setException(except);
        packet.setExceptionMsg(msg);
        packet.setHeader(14);
        return packet.build();
    }

    public static PacketOuterClass.Packet set (String key, String path, KasperObject object) {
        PreparedPacket packet = new PreparedPacket();
        packet.setHeader(1);
        packet.addArg("key", key);
        packet.addArg("path", path);
        packet.setData(object);
        return packet.build();
    }

    public static PacketOuterClass.Packet sendObjectResponse (KasperObject object, String path) {
        PreparedPacket packet = new PreparedPacket();
        packet.setHeader(15);
        packet.setData(object);
        packet.addArg("path", path);
        return packet.build();
    }

    public static PacketOuterClass.Packet hasProperty (String objectPath, String properties) {
        PreparedPacket packet = new PreparedPacket();
        packet.setHeader(6);
        packet.addArg("path", objectPath);
        packet.addArg("properties", properties);
        return packet.build();
    }

    public static boolean resolveExceptions (PacketOuterClass.Packet packet) throws Exception {
        int exception = packet.getException();
        String message = packet.getExceptionMessage();
        switch (exception) {
            case 1 -> throw new Exception(message);
            case 2 -> throw new KasperException(message);
            case 3 -> throw new KasperObjectAlreadyExists(message);
            case 4 -> throw new NotIterableException(message);
            case 5 -> throw new NoSuchKasperObject(message);
            default ->{return false;}
        }
    }

    public static int classifyException (Exception e) {
        if (e instanceof KasperException) {
            if (e instanceof KasperObjectAlreadyExists) return 3;
            if (e instanceof NotIterableException) return 4;
            if (e instanceof NoSuchKasperObject) return 5;
            return 2;
        } return 1;
    }
}
