package com.kasper.commons.authenticator;

import java.math.BigInteger;
import java.util.ArrayList;

public class Meta {

    public static boolean serverMode = false;

    public static int snapshotTimeout = 24000;

    // holds the data for concurrent sockets
    public static ArrayList<Integer> concurrentSockets = new ArrayList<>();
    public static String folder = "data/";
    public static String filename = "cluster.knf";

    public static String version = "Basalt 0.6.0";

    public static BigInteger maxRecursionDepth = BigInteger.valueOf(100000);

    public static String getPath(){
        return folder + filename;
    }

    public static int sample = 100000;
    public static int port = 53182;

    public static String backup = "backups/";
    public static String backupCounter(){
        if (module > 5) module = 0;
        return backup + "kasper_backup_unit[" + module++ + "].knf";
    }

    private static int module = 0;
    private static double requestTimeout = 5000;

    public static int maxConnections = 1026;


    public static void changePath (String path) {
        filename = path + ".knf";
    }
    public static int maxOperations = 400;
    private static int operationCounter = 0;
    public static void enqueueOperation() {
        operationCounter++;
        if (operationCounter > maxOperations)  {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored){}
        }

    }

    public static void dequeueOperation(){
        operationCounter--;
    }
}
