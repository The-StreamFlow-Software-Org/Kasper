package Persistence;

import com.kasper.Boost.JSONCache;
import datastructures.KasperNode;
import network.Pool;
import com.kasper.commons.Network.Timer;
import com.kasper.commons.Parser.ByteCompression;
import network.Lobby;
import network.Room;
import Server.Parser.AESUtils;
import Server.Parser.DiskIO;
import Server.SuperClass.KasperGlobalMap;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


// This class handles all global variables that need to be instantiated
// prior to accepting connections. It handles DiskIO, Keys, Meta Variables,
// serializing and deserializing, as well as security checks.
public class InstantiatorService {


    // Instantiates all global variables.
    @SuppressWarnings("unchecked")
    public static void start() throws IOException {
        DiskIO.writeConfig();
        Cache.init();
        KasperGlobalMap.instantiate();
        JSONCache.init();
        try {
            var s = DiskIO.getSerialized();
            KasperGlobalMap.getNodes();
            var temp =  Serialize.constructFromBlob(ByteCompression.decompress(AESUtils.decrypt(s)));
            if (temp != null) KasperGlobalMap.globalmap = (ConcurrentHashMap<String, KasperNode>) temp;
        }
        catch (IOException ignored){}
        catch (Exception e) {
            e.printStackTrace();
        }
        System.gc();

    }

    // closes this instance, including saving the most recent snapshots.
    // moreover, it forces the server to stop accepting connections.
    public static void  close() throws Exception {
        Lobby.ending = true; // stops asking for new connections
        Room.requestClose(); // signals for all the processes to gracefully close
        Room.ending = true; // stops asking for new requests
        System.out.println("Kasper:> Termination request received. Now gracefully shutting down all connections, threads, and processes.");
        Room.latch.await();
        Pool.shutdown();
        System.out.println("Kasper:> All processes have gracefully shut down.");
        System.out.println("Kasper:> Now saving data snapshots.");
        Timer.getTimer().start();
        DiskIO.writeDocument(AESUtils.encrypt(ByteCompression.compress(Serialize.writeToBytes(KasperGlobalMap.globalmap))));
    }

    // writes a snapshot as backup
    // it rotates through numbers 1 to 5
    public static void writeBackup() throws Exception{
        DiskIO.writeBackup(AESUtils.encrypt(Serialize.writeToBytes(KasperGlobalMap.globalmap)));
    }

}
