package Server.SuperClass;

import com.kasper.commons.Network.Timer;
import Server.Netty.MainLobby;

public class GlobalHolders {
    public static MainLobby mainLobby = new MainLobby();
    public static Timer timer = new Timer();

}
