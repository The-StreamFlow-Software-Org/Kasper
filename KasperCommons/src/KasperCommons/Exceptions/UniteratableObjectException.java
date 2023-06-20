package KasperCommons.Exceptions;

public class UniteratableObjectException extends KasperException{
    public UniteratableObjectException(String path) {
        super("Cannot iterate in path '" + path + "', as it is of type [string]. Only [map] and [list] are iterable.");
    }

}
