package network;

import com.kasper.commons.authenticator.KasperCommons.Authenticator.PacketOuterClass;
import com.kasper.commons.authenticator.KasperCommons.Authenticator.PreparedPacket;
import com.kasper.commons.authenticator.Meta;
import com.kasper.commons.Network.KasperNitroWire;
import com.kasper.commons.Network.NetworkPackageRunnable;
import com.kasper.commons.Parser.ExceptionAlias;
import com.kasper.commons.Parser.TokenSender;
import server.Handler.RequestHandler;

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
                    incrementProcess();
                    while (!ending) {
                        try {
                            var query = pack.get();
                            var packet = PacketOuterClass.Packet.parseFrom(query);
                            request.handleQuery(packet, pack);
                        } catch (IOException e) {
                            pack.close();
                            decrementProcesses();
                            break;
                        } catch (Exception e) {
                            var except = TokenSender.raise(ExceptionAlias.KASPER_EXCEPTION, "Thrown by KasperEngine: Reason:> Invalid Nitro Wire exception. See engine logs for details.\n");
                            try {
                                pack.put(except.toByteArray());
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            e.printStackTrace();
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                    pack.close();

                } finally {
                    decrementProcesses();
                }
            }
        });
    }

    public static void incrementProcess(){
        Meta.enqueueOperation();
        ongoingProcesses++;
    }

    private static void decrementProcesses(){
        ongoingProcesses--;
        Meta.dequeueOperation();
        if (!requestClose) return;
        if (ongoingProcesses <= 0) latch.countDown();
    }


    public static void requestClose (){
        requestClose = true;
        if (ongoingProcesses <= 0) latch.countDown();
    }
    private static boolean requestClose = false;



}
