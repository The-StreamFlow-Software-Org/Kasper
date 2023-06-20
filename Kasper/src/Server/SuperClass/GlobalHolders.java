package Server.SuperClass;

import KasperCommons.Network.Timer;
import Server.Handler.RequestHandler;
import Server.Netty.MainLobby;

public class GlobalHolders {
    public static MainLobby mainLobby = new MainLobby();
    public static Timer timer = new Timer();

}
