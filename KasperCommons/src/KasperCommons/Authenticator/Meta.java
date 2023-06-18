package KasperCommons.Authenticator;

public class Meta {

    public static int snapshotTimeout = 24000;
    public static String folder = "data/";
    public static String filename = "cluster.knf";

    public static int maxRecursionDepth = 1000;

    public static String getPath(){
        return folder + filename;
    }

    public static int sample = 100000;
    public static int port = 53182;

    public static String backup = "backups/";
    public static String backupCounter(){
        if (module > 5) module = 0;
        return backup + "loginfo" + module++;
    }

    private static int module = 0;


    public static void changePath (String path) {
        filename = path + ".knf";
    }
}
