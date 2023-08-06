package nio.kasper;

import com.kasper.Boost.JSONCache;
import com.kasper.commons.authenticator.Meta;
import com.kasper.commons.debug.W;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import server.Handler.AsyncServerTasks;
import server.Parser.DiskIO;

import java.net.InetAddress;
import java.net.InetSocketAddress;


/**
 * The orchestrator class is in-charge of dispatching
 * server sockets from netty.
 * The orchestrator class can take in multiple threads of
 * listening in order to accommodate concurrently
 * connecting clients.
 */
public class Orchestrator {
    private EventLoopGroup platform;
    private EventLoopGroup rooms;
    private ServerBootstrap bootstrap;
    private Channel channel;

    public Orchestrator () {
        W.rite("[Nitro StreamFlow IO] Starting orchestration service.");
        JSONCache.init();
        bootstrap = new ServerBootstrap();
        platform = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        rooms = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
    }

    public void start () {
        AsyncServerTasks.persistenceSnapshots();
        try {
            bootstrap.group(platform, rooms)
                    .localAddress(new InetSocketAddress(Meta.port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new NioPacketEncoder(),
                                    new NioPacketDecoder(),
                                    new NitroChannel());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 10000)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .channel(NioServerSocketChannel.class);
            channel = bootstrap.bind().sync().channel();
            AsyncServerTasks.exitHandler(this);
            W.rite("[Nitro StreamFlow IO] Orchestration service success.");
            W.rite("[Nitro StreamFlow IO] Server address identified. [" + InetAddress.getLocalHost().getHostAddress() + "]");
        } catch (Exception e) {
            W.error(e, "[Nitro StreamFlow IO] There was an error during the orchestration service.");
        }
    }

    public void stop() throws InterruptedException {
        if (channel != null) {
            channel.close().sync();
        }
        platform.shutdownGracefully();
        rooms.shutdownGracefully();
        System.out.println("-----------------------------------");
        W.rite("[Nitro StreamFlow IO] orchestration service stopped.");
    }

}
