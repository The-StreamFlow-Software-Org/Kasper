package nio.kasper;

import com.kasper.commons.Parser.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
public class NioPacketDecoder extends ByteToMessageDecoder {
    private static final int HEADER_SIZE = 5;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        if (byteBuf.readableBytes() < HEADER_SIZE) {
            return;
        }

        byteBuf.markReaderIndex();
        byte method = byteBuf.readByte();

        byte[] intPad = new byte[4];
        for (int i=0; i<4; i++) {
            intPad[i] = byteBuf.readByte();
        }
        int length = ByteUtils.bytesToInt(intPad);

        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] data = new byte[length];
        byteBuf.readBytes(data);

        list.add(new NioPacket(method, data));
    }
}
