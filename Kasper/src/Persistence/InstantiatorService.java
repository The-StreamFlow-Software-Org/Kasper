package Persistence;

import Boost.JSONCache;
import DataStructures.KasperNode;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.Authenticator.Meta;
import KasperCommons.Concurrent.Pool;
import KasperCommons.Exceptions.InvalidPersistenceData;
import KasperCommons.Network.NetworkPackageRunnable;
import KasperCommons.Network.Operations;
import KasperCommons.Parser.ByteCompression;
import Server.Parser.AESUtils;
import Server.Parser.DiskIO;
import KasperCommons.Parser.KasperDocument;
import Server.SuperClass.KasperGlobalMap;
import Server.SuperClass.SocketHolders;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class InstantiatorService {

    protected static void initConcurrentSockets() throws IOException {
        System.out.println("Kasper:> Instantiating concurrent sockets.");
        SocketHolders.socket1 = new ServerSocket(Meta.concurrentSockets.get(0));
        SocketHolders.socket2 = new ServerSocket(Meta.concurrentSockets.get(1));
        SocketHolders.socket3 = new ServerSocket(Meta.concurrentSockets.get(2));
        SocketHolders.socket4 = new ServerSocket(Meta.concurrentSockets.get(3));
        SocketHolders.socket5 = new ServerSocket(Meta.concurrentSockets.get(4));
    }

    @SuppressWarnings("unchecked")
    public static void start() throws IOException {
        DiskIO.writeConfig();
        Cache.init();
        KasperGlobalMap.instantiate();
        new KasperAccessAuthenticator("kasper.util.key");
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

    public static void  close() throws Exception {
        DiskIO.writeDocument(AESUtils.encrypt(ByteCompression.compress(Serialize.writeToBytes(KasperGlobalMap.globalmap))));
    }

    public static void writeBackup() throws Exception{
        DiskIO.writeBackup(AESUtils.encrypt(Serialize.writeToBytes(KasperGlobalMap.globalmap)));
    }


    private static InstantiatorService instance = null;

    public static boolean finish = false;

    public static void consumeMigration() {
        if (instance == null) instance = new InstantiatorService();
        instance.startNonStatic();
        instance = null;
    }

    private void startNonStatic () {
        Pool.newThread(new NetworkPackageRunnable() {
            @Override
            public void run() {

            }
        });

        try {
            System.out.println("Kasper:> Constructor service has started. Unzipping and decrypting binaries.");
            Operations.reset();
            Scanner scan = new Scanner(System.in);
            //scan.nextLine();
            var construct = DiskIO.documentStringRetrieval();
            Pool.newThread(new NetworkPackageRunnable() {
                @Override
                public void run() {
                    while (!finish) {
                        System.out.println("Kasper:> This may take a while. We are parsing binaries, parsed " + Operations.getOperations() + " items so far.");
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            var data = KasperDocument.constructor(construct);
            finish = true;
            var document = data.getDocument(KasperAccessAuthenticator.getKey());
            var query = document.getElementsByTagName("query").item(0);
            var queryChild = query.getChildNodes();
            var purpose = queryChild.item(0);
            if (!purpose.getTextContent().equals("reconstruction"))
                throw new InvalidPersistenceData("The persistence data has an invalid header for args, provided: '" + purpose.getTextContent() + "' instead of 'reconstruction'");
            finish = false;
            Pool.newThread(new NetworkPackageRunnable() {
                @Override
                public void run() {
                    while (!finish) {
                        System.out.println("Kasper:> Please wait, this may take a while. We are constructing your snapshots, loaded " + Operations.getOperations() + " objects so far.");
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            var args = queryChild.item(1);
            var nodes = args.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                var node = nodes.item(i);
                var attrib = node.getChildNodes();
                KasperGlobalMap.getNodes().put(attrib.item(0).getTextContent(), new KasperNode(node));
            }
            document = null;
            data = null;
            System.gc();
            //System.out.println(document);

        } catch (IOException e){
            KasperGlobalMap.getNodes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public static void writeMigration() throws Exception {
        var x = KasperGlobalMap.getNodes();
        var iteratorset = x.entrySet();
        Outstream root = null;
        Operations.reset();
        finish = false;
        Pool.newThread(new NetworkPackageRunnable() {
            @Override
            public void run() {
                while (!finish) {
                    System.out.println("Kasper:> Please wait, this may take a while. We are bucketizing your nodes, " + Operations.getOperations() + " objects are ready for dispatch.");
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        for (var i : iteratorset){
            if (root == null) {
                root = new Outstream(i.getValue());
            } else {
                root.chain(new Outstream(i.getValue()));
            }
        }
        finish = true;
        root.bucketize();
        Pool.shutdown();
    }
}
