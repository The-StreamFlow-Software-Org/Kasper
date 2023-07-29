package nio.kasper;

import com.kasper.commons.Parser.ByteUtils;
import com.kasper.commons.debug.W;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public class NioPacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        W.rite("Attempting decode.");
        // Check if there are at least 4 bytes available for the length header
        if (byteBuf.readableBytes() < 4) {
            W.rite("Cannot write decode because there is not enough readable bytes.");
            return;
        }

        byteBuf.markReaderIndex();
        int length = 0;
        // Read the length header
        if (byteBuf.readableBytes() < 4) {
            // Not enough data available, reset the reader index and return
            byteBuf.resetReaderIndex();
            W.rite("Cannot write decode because not enough int padding available.");
            return;
        }

        byte[] intPad = new byte[4];
        for (int i=0; i<4; i++) {
            intPad[i] = byteBuf.readByte();
        }
        System.out.println("Int padding: " + Arrays.toString(intPad));
        length = ByteUtils.bytesToInt(intPad);



        System.out.println("Readable bytes: " + length);

        // Check if the full message is available
        if (byteBuf.readableBytes() < length) {
            // Not enough data available, reset the reader index and return
            byteBuf.resetReaderIndex();
            W.rite("Cannot write decode because not enough message available.");
            return;
        }

        // If the full message is available, read it into a byte array
        byte[] data = new byte[length];
        byteBuf.readBytes(data);

        // Create a new NioPacket with the data and add it to the list
        list.add(new NioPacket(data));
    }
}
