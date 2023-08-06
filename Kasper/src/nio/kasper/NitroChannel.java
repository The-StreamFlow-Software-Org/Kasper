package nio.kasper;

import Persistence.InstantiatorService;
import Persistence.Serialize;
import com.kasper.commons.Network.Timer;
import com.kasper.commons.Parser.ByteCompression;
import com.kasper.commons.aliases.Method;
import com.kasper.commons.debug.W;
import com.kasper.commons.exceptions.KasperException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import server.Parser.AESUtils;
import server.Parser.DiskIO;
import server.SuperClass.KasperGlobalMap;

import java.net.SocketException;

public class NitroChannel implements ChannelInboundHandler {

    private static boolean authenticated = false;

    private static int process_counter = 0;
    private synchronized static void increment() {
        process_counter++;
    }

    private synchronized static void decrement () {
        process_counter--;
        if (process_counter <= 0 && stopping) {
            try {
                Timer.getTimer().start();
                W.rite("[Persistence] The server is shutting down. Saving data snapshots.");
                DiskIO.writeDocument(AESUtils.encrypt(ByteCompression.compress(Serialize.writeToBytes(KasperGlobalMap.globalmap))));
                System.out.println("Kasper:> [SIGINT Event Handler] Instantiating the closing service. To force close the server, use 'ctrl + c' again. Warning: This may cause data loss.");
                orchestrator.stop();
                System.out.println("Kasper:> [Persistence] Data snapshots saved after " + Timer.getTimer().stop() + "s.");
                System.out.println("Kasper:> [StreamFlow Staff Messaging] Kasper says bye! :)");
                W.rite("[IO Mutex] Destroying the IO Mutex.");
                InstantiatorService.unlockThisServer();
                W.rite("[IO Mutex] IO Mutex destroyed.");
                System.exit(0);
            } catch (Exception e) {
                System.out.println("Kasper:> [Persistence] An exception occurred when saving the data snapshots. Please check the backups.");
                System.exit(0);
            }
        }
    }

    private static Orchestrator orchestrator = null;

    private static boolean stopping = false;

    public static synchronized void requestStop (Orchestrator orchestrator) {
        NitroChannel.orchestrator = orchestrator;
        stopping = true;
        decrement();
    }
    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        StagedResultSet set = new StagedResultSet();


    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        increment();
        if (o instanceof NioPacket packet) {
            StagedResultSet resultSet = null;
            try {
                if (packet.getMethod() == Method.AUTH) {
                    try {
                        packet.assertAuth();
                    } catch (KasperException e) {
                        resultSet = new StagedResultSet();
                        resultSet.addResult(e);
                        channelHandlerContext.writeAndFlush(resultSet.getBytes());
                        channelHandlerContext.close();
                        return;
                    }
                    resultSet = new StagedResultSet();
                    // handle the authentication here
                    // currently, the authentication process
                    // is insecure. We may need to change
                    // it in the future.
                    resultSet.addQueryOk();
                    authenticated = true;
                }
                else {
                    if (authenticated)
                    resultSet = packet.executeQuery();
                    else {
                        channelHandlerContext.close();
                        return;
                    }
                }
            } catch (KasperException e) {
                if (resultSet == null) {
                    resultSet = new StagedResultSet();
                }
                resultSet.addResult(e);
            } catch (Exception e) {
                if (resultSet == null) {
                    resultSet = new StagedResultSet();
                }
                resultSet.addResult(new KasperException(e));
            }
            channelHandlerContext.writeAndFlush(resultSet.getBytes());
        } else {
            W.rite("Unknown error when reading packet.");
        }
        decrement();
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
