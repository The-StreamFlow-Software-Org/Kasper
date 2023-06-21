package KasperCommons.Authenticator.KasperCommons.Authenticator;


import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Network.Timer;
import KasperCommons.Parser.JSONUtils;

import java.util.HashMap;
import java.util.Map;


public class PreparedPacket{
    protected PacketOuterClass.Packet.Builder builder;
    protected Map<String, String> argsBuffer;


    public PreparedPacket() {
        builder = PacketOuterClass.Packet.newBuilder();
        argsBuffer = new HashMap<>();
    }

    /**
     Args type:<br>
     { 'path' : contains the path }<br>
     { 'uri' : contains the URI }<br>
     { 'match-path' : contains the path to match in 'has-property' methods }<br>
     */

    public PreparedPacket addArg(String argType, String argMsg) {
        argsBuffer.put(argType, argMsg);
        return this;
    }

    /**
     1 -> set<br>
     2 -> get<br>
     3 -> create-node<br>
     4 -> create-collection<br>
     5 -> exists<br>
     6 -> has-property<br>
     7 -> has-property-equal-to<br>
     8 -> delete<br>
     9 -> update<br>
     10 -> full-text-search<br>
     11 -> auth-simply<br>
     12 -> auth-uri<br>
     13 -> response-ok<br>
     14 -> response-error<br>
     15 -> response-ok-with-args<br>
     */

    public PreparedPacket setHeader (int header) {
        builder.setHeader(header);
        return this;
    }

    public PreparedPacket setData(KasperObject object) {
        Timer t = new Timer();
       t.start();
        var stringified = JSONUtils.objectToJsonStream(object);
        System.out.println("GSON overhead is: " + t.stop());
        builder.setData(stringified);
        return this;
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

    public PreparedPacket setException (int exceptionType){
        builder.setException(exceptionType);
        return this;
    }

    public PreparedPacket setExceptionMsg(String exceptMsg){
        builder.setExceptionMessage(exceptMsg);
        return this;
    }

    public PacketOuterClass.Packet build (){
        builder.putAllArgs(argsBuffer);
        return builder.build();
    }

    public PacketOuterClass.Packet.Builder builder() {
        return builder;
    }
}
