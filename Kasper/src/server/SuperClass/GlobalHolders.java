package server.SuperClass;

import com.kasper.commons.Network.Timer;
import server.Netty.MainLobby;

public class GlobalHolders {
    public static MainLobby mainLobby = new MainLobby();
    public static Timer timer = new Timer();
    public static String[] args = null;
    public static int argc = 0;

}
