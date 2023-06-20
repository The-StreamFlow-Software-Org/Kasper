package KasperCommons.Exceptions;

import java.io.Serial;

public class NotIterableException extends KasperException{
    @Serial
    private static final long serialVersionUID = 5433446864760277796L;

    public NotIterableException (String path) {
        super("Cannot iterate in path '" + path + "', as it is of type [string]. Only [map] and [list] are iterable.");
    }

}
