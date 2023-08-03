package Persistence;

import com.kasper.commons.authenticator.Meta;
import parser.ParseProcessor;
import server.Parser.AESUtils;
import server.Parser.DiskIO;
import server.SuperClass.GlobalHolders;
import server.SuperClass.KasperGlobalMap;
import com.kasper.Boost.JSONCache;
import com.kasper.commons.Network.Timer;
import com.kasper.commons.Parser.ByteCompression;
import com.kasper.commons.exceptions.KasperException;
import datastructures.KasperNode;
import network.Lobby;
import network.Pool;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;


// This class handles all global variables that need to be instantiated
// prior to accepting connections. It handles DiskIO, Keys, Meta Variables,
// serializing and deserializing, as well as security checks.
public class InstantiatorService {

    public static FileLock lock = null;
    public static FileChannel channel = null;
    public static boolean lockedByThis = false;
    public static String mutexLocation = System.getProperty("user.home")+ "/kasper.mutex";

    public static void lockThisServer() {
        System.out.println("Kasper:> [IO Mutex] Checking IO mutex lock.");
        try {
            channel = new RandomAccessFile(mutexLocation, "rw").getChannel();
            lock = channel.tryLock();
            if (lock == null) {
                System.err.println("Kasper:> [IO Mutex] IO Mutex lock failed. Cannot run double instances of the Kasper Engine. Please terminate the other instance first.");
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println(mutexLocation);
            e.printStackTrace();
            throw new KasperException("Kasper:> [IO Mutex] Cannot use the mutex lock to lock this Kasper Engine instance.");
        }
        lockedByThis = true;
        System.out.println("Kasper:> [IO Mutex] Successfully locked this instance. Mutexes instantiated.");
    }

    public static void unlockThisServer() {
        try {
            if (lock != null) {
                lock.release();
                lock = null;
            }
            if (channel != null) {
                channel.close();
                channel = null;
            }
        } catch (IOException e) {
            throw new KasperException(" [IO Mutex] Cannot use the mutex lock to unlock this Kasper Engine instance.");
        }
        System.out.println("Kasper:> [IO Mutex] Successfully unlocked this instance. Mutexes destroyed.");
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
        if (GlobalHolders.argv.length > 0) {
            // CHECK FOR REPL
            if (GlobalHolders.argv[0].equals("-repl")) {
                System.out.println("Instantiating Kasper REPL");
                REPL();
            }
        }
    }

    public static void REPL () {
        try {
            ParseProcessor processor = new ParseProcessor();
            String base = "kasper:" + InetAddress.getLocalHost().getHostAddress() + "://" + Meta.port + "> ";
            Scanner scan = new Scanner(System.in);
            while (true) {
                System.out.print(base);
                var evaluate = scan.nextLine();
                if (evaluate.isEmpty()) continue;
                try {
                    processor.consumeString(evaluate);
                    System.out.println("Query Ok!");
                } catch (Exception e) {
                    System.out.println("\n-------------------");
                    System.out.println("Error Message");
                    System.out.println("-------------------");
                    System.out.println("\n");
                    System.err.println(e.getMessage());
                    System.out.println("\n");
                    System.out.println("-------------------\n");
                }
            }
        } catch (UnknownHostException e) {
            throw new KasperException(e.getMessage());
        }
    }

    // closes this instance, including saving the most recent snapshots.
    // moreover, it forces the server to stop accepting connections.
    public static void  close() throws Exception {
        Lobby.ending = true; // stops asking for new connections
        unlockThisServer(); // unlocks the IO mutex
        System.out.println("Kasper:> Termination request received. Now gracefully shutting down all connections, threads, and processes.");
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
