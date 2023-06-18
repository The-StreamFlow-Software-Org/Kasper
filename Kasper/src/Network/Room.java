package Network;

import KasperCommons.Concurrent.Pool;
import KasperCommons.Network.NetworkPackage;
import KasperCommons.Network.NetworkPackageRunnable;
import KasperCommons.Parser.KasperDocument;
import Server.Handler.RequestHandler;

import java.io.IOException;
import java.net.SocketException;

public class Room {
    private NetworkPackage pack;


    public Room (NetworkPackage pack) {
        this.pack = pack;
        handleMethods();
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
                        var document = KasperDocument.constructor(query);
                        assert document != null;
                        request.handleQuery(document, pack);
                    } catch (SocketException e) {
                        var x = e;
                        return;
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

}
