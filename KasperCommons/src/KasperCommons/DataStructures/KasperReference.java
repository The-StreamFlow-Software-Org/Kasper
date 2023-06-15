package KasperCommons.DataStructures;

import java.io.Serial;

public class KasperReference extends KasperObject{


    @Serial
    private static final long serialVersionUID = 4299160416314836746L;

    public KasperReference(String path) {
        super("reference");
        data = path;
    }
}
