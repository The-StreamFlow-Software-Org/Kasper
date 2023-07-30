package nio.kasper;

import com.google.gson.JsonParseException;
import com.kasper.commons.Parser.ByteUtils;
import com.kasper.commons.aliases.Method;
import com.kasper.commons.authenticator.Meta;
import com.kasper.commons.datastructures.JSONUtils;
import com.kasper.commons.datastructures.KasperList;
import com.kasper.commons.datastructures.KasperMap;
import com.kasper.commons.datastructures.KasperObject;
import com.kasper.commons.debug.W;
import io.netty.channel.ChannelHandlerContext;
import parser.ParseProcessor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class NioPacket {
    private byte[] packetBytes;
    private final int method;

    // Stages a parse exception
    public byte[] raiseException (Exception e) {
        KasperMap map = new KasperMap();
        map.put("exception", e.getMessage());
        String jsonMap = JSONUtils.objectToJsonStream(map);
        byte[] bytes = jsonMap.getBytes(StandardCharsets.UTF_16);
        var x = ByteUtils.intToBytes(bytes.length);
        byte[] result = new byte[bytes.length + 5];
        result[0] = (byte)Method.RESPONSE_QUERY;
        System.arraycopy(x, 0, result, 1, 4);
        System.arraycopy(bytes, 0, result, 5, bytes.length);
        return result;
    }

    // Stages the valid result set into
    public static byte[] stageData (KasperList list) throws Exception {
        var set = JSONUtils.objectToJsonStream(list);
        var bytes = set.getBytes(StandardCharsets.UTF_16);
        var lengthBytes = ByteUtils.intToBytes(bytes.length);
        byte method = (byte)Method.RESPONSE_QUERY;
        byte[] result = new byte[bytes.length + 5];
        result[0] = method;
        System.arraycopy(lengthBytes, 0, result, 1, 4);
        System.arraycopy(bytes, 0, result, 5, bytes.length);
        return result;
    }


    public boolean assertAuth() throws IOException {
        if (method != Method.AUTH) return false;
        KasperMap map = JSONUtils.parseJson(new String(packetBytes, StandardCharsets.UTF_16)).castToMap();
        Meta.assertDefault(map.get("username").toStr(), map.get("password").toStr());
        return true;
    }

    public int method() {
        return method;
    }

    public NioPacket(byte method, byte[] packetBytes) {
        this.method = method;
        this.packetBytes = packetBytes;
    }

    public void executeQuery () {
        ParseProcessor processor = new ParseProcessor();
        processor.executeQuery(new String(packetBytes, StandardCharsets.UTF_16));
    }
}
