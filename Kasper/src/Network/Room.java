package Network;

import KasperCommons.Authenticator.KasperCommons.Authenticator.PacketOuterClass;
import KasperCommons.Authenticator.KasperCommons.Authenticator.PreparedPacket;
import KasperCommons.Authenticator.Meta;
import KasperCommons.Concurrent.Pool;
import KasperCommons.Network.KasperNitroWire;
import KasperCommons.Network.NetworkPackageRunnable;
import Server.Handler.RequestHandler;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

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

    public static boolean ending = false;
    public static CountDownLatch latch = new CountDownLatch(1);
    private static int ongoingProcesses = 0;
    public void handleMethods(){
        RequestHandler request = new RequestHandler();
        Pool.newThread(new NetworkPackageRunnable() {
            @Override
            public void run() {
                try {
                    this.net = pack;
                    ongoingProcesses++;
                    while (!ending) {
                        try {
                            var query = pack.get();
                            var packet = PacketOuterClass.Packet.parseFrom(query);
                            request.handleQuery(packet, pack);
                        } catch (IOException e) {
                            try {
                                pack.close();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            break;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        pack.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } finally {
                    decrementProcesses();
                }
            }
        });
    }

    private static void decrementProcesses(){
        ongoingProcesses--;
        if (!requestClose) return;
        if (ongoingProcesses == 0) latch.countDown();
    }


    public static void requestClose (){
        requestClose = true;
        if (ongoingProcesses == 0) latch.countDown();
    }
    private static boolean requestClose = false;



}
