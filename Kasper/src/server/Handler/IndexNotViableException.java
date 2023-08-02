package server.Handler;

import com.kasper.commons.exceptions.KasperException;

public class IndexNotViableException extends KasperException {
    public IndexNotViableException(String path, String index) {
        super("The index is not viable. The object at path'" + path
        + "' cannot access the index '" + index + "'. Indices must be either 'head' or 'tail'.");
    }
}
