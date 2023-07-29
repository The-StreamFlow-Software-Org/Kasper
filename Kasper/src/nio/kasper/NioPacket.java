package nio.kasper;

import parser.ParseProcessor;

import java.nio.charset.StandardCharsets;

public class NioPacket {
    private static byte[] packetBytes;

    public NioPacket(byte[] packetBytes) {
        packetBytes = packetBytes;
    }

    public void executeQuery () {
        ParseProcessor processor = new ParseProcessor();
        processor.executeQuery(new String(packetBytes, StandardCharsets.UTF_16));
    }
}
