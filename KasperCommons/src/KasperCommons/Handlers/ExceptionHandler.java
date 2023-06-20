package KasperCommons.Handlers;

import KasperCommons.Exceptions.KasperException;
import KasperCommons.Exceptions.NoSuchKasperObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ExceptionHandler {

    public static void attemptException(String exception, String msg) {
        try {
            var loaded = ClassLoader.getSystemClassLoader().loadClass(exception).getConstructor(String.class);
            var x = loaded.newInstance(msg);
            throw (KasperException) x;
        } catch (ClassCastException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException x) {
            throw new KasperException(x.getMessage());
        }
    }
}
