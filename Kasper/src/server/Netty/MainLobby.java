package server.Netty;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class MainLobby {
    protected EventLoopGroup boss;
    protected EventLoopGroup worker;

    public void start () {

    }

    public MainLobby () {
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
    }

}
