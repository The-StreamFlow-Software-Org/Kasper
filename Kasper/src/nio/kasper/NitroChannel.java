package nio.kasper;

import com.kasper.commons.debug.W;
import com.kasper.commons.exceptions.KasperException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class NitroChannel implements ChannelInboundHandler {
    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if (o instanceof NioPacket packet) {
            StagedResultSet resultSet = new StagedResultSet();
            try {
                if (packet.assertAuth()) {
                    // handle the authentication here
                    // currently, the authentication process
                    // is insecure. We may need to change
                    // it in the future.
                    resultSet.addQueryOk();
                }
                else packet.executeQuery(resultSet);
            } catch (KasperException e) {
                resultSet.addResult(e);
            } catch (Exception e) {
                resultSet.addResult(new KasperException(e));
            }
            channelHandlerContext.writeAndFlush(
                    NioPacket.stageData(resultSet.getBytes()));
        } else {
            W.rite("Unknown error when reading packet.");
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {
        if (throwable instanceof SocketException) return;
        W.error(throwable, "Caught an exception in client. Check error logs for more information.");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

    }
}
