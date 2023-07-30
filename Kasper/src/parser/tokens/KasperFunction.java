package parser.tokens;

import com.kasper.commons.datastructures.KasperObject;

import java.util.ArrayList;
import java.util.LinkedList;

public interface KasperFunction {

    void performTask(LinkedList<KasperObject> arguments);
}
