package Network;

import KasperCommons.Concurrent.Pool;
import KasperCommons.Network.NetworkPackage;
import KasperCommons.Parser.KasperDocument;
import Server.Handler.RequestHandler;

import java.io.IOException;

public class Room {
    private NetworkPackage pack;


    public Room (NetworkPackage pack) {
        this.pack = pack;
        handleMethods();
    }

    public void handleMethods(){
        Pool.newThread(() -> {
            while (true) {
                try {
                    var query = pack.get();
                    var document = KasperDocument.constructor(query);
                    RequestHandler request = new RequestHandler();
                    assert document != null;
                    request.handleQuery(document, pack);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
