package com.kasper.commons.Handlers;

import com.kasper.commons.exceptions.KasperException;

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
