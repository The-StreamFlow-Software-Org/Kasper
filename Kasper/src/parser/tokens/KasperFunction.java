package parser.tokens;

import com.kasper.commons.datastructures.KasperObject;

import java.util.ArrayList;

public interface KasperFunction {

    void performTask(ArrayList<KasperObject> arguments);
}
