package KasperCommons.Authenticator.KasperCommons.Authenticator;

import PacketOuterClass;
import KasperCommons.DataStructures.KasperObject;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class PreparedPacket{
    protected PacketOuterClass.Packet.Builder builder;
    protected Map<String, String> argsBuffer;


    public PreparedPacket() {
        builder = PacketOuterClass.Packet.newBuilder();
        argsBuffer = new HashMap<>();
    }

    public PreparedPacket addArg(String argType, String argMsg) {
        argsBuffer.put(argType, argMsg);
        return this;
    }

    public PreparedPacket addData(KasperObject object) {
        Gson gson = new Gson();
        var stringified = gson.toJson(object);
        builder.setData(stringified);
        return this;
    }

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
