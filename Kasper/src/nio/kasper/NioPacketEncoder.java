package nio.kasper;

import com.kasper.commons.Parser.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NioPacketEncoder extends MessageToByteEncoder<StagedResultSet> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, StagedResultSet set, ByteBuf byteBuf) throws Exception {
        var bytes = set.getBytes();
        byteBuf.writeBytes(ByteUtils.intToBytes(bytes.length));
        byteBuf.writeBytes(bytes);
    }
}
