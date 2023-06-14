package KasperCommons.Network;

public class Operations {
    private static int operations;

    public static void incrementOperation(){
        operations++;
    }

    public static int getOperations (){
        return operations;
    }

    public static void reset () {
        operations = 0;
    }
}
