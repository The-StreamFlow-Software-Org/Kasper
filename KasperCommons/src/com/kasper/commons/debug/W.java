package com.kasper.commons.debug;

import com.kasper.commons.Handlers.LogWriter;

public class W {
    public static void rite (Object ... o) {
        System.out.print("Kasper:> ");
        for (var x : o) {
            System.out.print(x);
        }
        System.out.println();
    }

    public static void error (Exception e, Object o) {
        System.out.print("Kasper:> ");
        System.out.print(o);
        System.out.print(" Check your error logs for more information.");
        System.out.println();
        LogWriter.writeLog(e);
    }

    public static void error (Throwable e, Object o) {
        System.out.print("Kasper:> ");
        System.out.print(o);
        System.out.print(" Check your error logs for more information.");
        System.out.println();
        LogWriter.writeLog(new Exception(e));
    }

    public static void errorFlush (Exception e, Object o) {
        System.out.print("Kasper:> ");
        System.out.print(o);
        System.out.print(" Reason:> \n");
        e.printStackTrace();
        System.out.println();
    }
}
