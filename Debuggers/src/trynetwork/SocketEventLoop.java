package trynetwork;

import com.kasper.commons.exceptions.KasperException;
import com.kasper.commons.Network.NitroPacket;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;

public class SocketEventLoop {
    private ServerSocket socket;
    public SocketEventLoop(ServerSocket socket) {
        this.socket = socket;
    }

    public void startLoop(){
        while (true) {
            try {
                NitroPacket packet = new NitroPacket(socket.accept());
                Thread t = new Thread(()-> {
                    while (true) {
                        try {
                            System.out.println(new String(packet.read(), StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                t.start();
            } catch (Exception e) {
                throw new KasperException(e.getMessage());
            }
        }
    }
}
