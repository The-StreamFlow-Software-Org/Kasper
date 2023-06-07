package KasperCommons.DataStructures;

/**
 * The most basic of the Kasper data structures,
 * it is a primitive data structure that holds a string.
 */
public class KasperString extends KasperObject{

    public KasperString(String data) {
        super("string");
        this.data = data;
    }

    @Override
    public String automaticallyCast() {
        return toString();
    }
}
