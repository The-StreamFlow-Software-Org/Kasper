package Persistence;

import server.Parser.AESUtils;
import server.Parser.DiskIO;
import server.SuperClass.KasperGlobalMap;
import com.kasper.Boost.JSONCache;
import com.kasper.commons.Network.Timer;
import com.kasper.commons.Parser.ByteCompression;
import com.kasper.commons.exceptions.KasperException;
import datastructures.KasperNode;
import network.Lobby;
import server.locals.LocalServer;
import network.Pool;
import network.Room;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


// This class handles all global variables that need to be instantiated
// prior to accepting connections. It handles DiskIO, Keys, Meta Variables,
// serializing and deserializing, as well as security checks.
public class InstantiatorService {

    public static boolean lockedByThis = false;
    public static String mutexLocation = System.getProperty("user.home")+ "/kasper.mutex";

    public static void lockThisServer() {
        System.out.println("Kasper:> Checking IO mutex lock.");
        File file = new File(mutexLocation);
            if (file.exists()) {
                System.err.println("Kasper:> IO Mutex lock failed. Cannot run double instances of the Kasper Engine. Please terminate the other instance first.");
                if (InstantiatorService.lockedByThis) InstantiatorService.unlockThisServer();
                System.exit(0);
            }
        try (BufferedWriter write = new BufferedWriter(new FileWriter(mutexLocation))){
            write.write(LocalServer.timestampNow());
            lockedByThis = true;
        } catch (IOException e) {
            System.out.println(mutexLocation);
            e.printStackTrace();
            throw new KasperException("Kasper:> Cannot use the mutex lock to lock this Kasper Engine instance.");
        }
        System.out.println("Kasper:> Successfully locked this instance. Mutexes instantiated.");
    }

    public static void unlockThisServer() {
       File file = new File(mutexLocation);
       if (!file.delete())
            throw new KasperException("Cannot use the mutex lock to unlock this Kasper Engine instance.");
        System.out.println("Kasper:> Successfully unlocked this instance. Mutexes destroyed.");
    }


    // Instantiates all global variables.
    @SuppressWarnings("unchecked")
    public static void start() throws IOException {
        DiskIO.writeConfig();
        lockThisServer(); // locks the IO mutex
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
        unlockThisServer(); // unlocks the IO mutex
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
