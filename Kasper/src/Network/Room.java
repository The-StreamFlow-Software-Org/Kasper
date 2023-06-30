package Network;

import KasperCommons.Authenticator.KasperCommons.Authenticator.PacketOuterClass;
import KasperCommons.Authenticator.KasperCommons.Authenticator.PreparedPacket;
import KasperCommons.Authenticator.Meta;
import KasperCommons.Concurrent.Pool;
import KasperCommons.Network.KasperNitroWire;
import KasperCommons.Network.NetworkPackageRunnable;
import Server.Handler.RequestHandler;

import java.io.IOException;

public class Room {
    private KasperNitroWire pack;


    public Room (KasperNitroWire pack)  {
        this.pack = pack;
        handleMethods();

    }



    public void initConcurrentSockets() throws IOException {
        PreparedPacket packet =  new PreparedPacket();
        packet.setHeader(0);
        for (int i=0; i<5; i++){
            packet.addArg(Integer.toString(i), Integer.toString(Meta.concurrentSockets.get(i)));
        }   pack.put(packet.build().toByteArray());

    }

    public void handleMethods(){
        RequestHandler request = new RequestHandler();
        Pool.newThread(new NetworkPackageRunnable() {
            @Override
            public void run() {
                this.net = pack;
                while (true) {
                    try {
                        var query = pack.get();
                        var packet = PacketOuterClass.Packet.parseFrom(query);
                        request.handleQuery(packet, pack);
                    }
                    catch (IOException e) {
                        try {
                            pack.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        } break;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

}
