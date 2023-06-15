package Server.SuperClass;

public class Meta {
    public static String folder = "data/";
    public static String filename = "cluster.knf";

    public static String getPath(){
        return folder + filename;
    }

    public static int sample = 100000;

    public static void changePath (String path) {
        filename = path + ".knf";
    }
}
