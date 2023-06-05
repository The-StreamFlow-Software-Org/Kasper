package DataStructures;

import java.util.ArrayList;

public class KasperList extends KasperObject{


    public KasperList() {
        super("list");
        data = new ArrayList<KasperObject>();
    }

    public void addToList(KasperObject object) {
        toList().add(object);
    }



}
