package com.kasper.commons.Handlers;

import com.kasper.locals.LocalServer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

public class LogWriter {
    public static void writeLog(Exception log) {
        StringWriter logMsg = new StringWriter();
        log.printStackTrace(new java.io.PrintWriter(logMsg));
        try (BufferedWriter write = new BufferedWriter(new FileWriter("data/log.txt", true))){
            write.write("Timestamp: " + LocalServer.timestampNow() + "\n");
            write.write(logMsg.toString());
        } catch (IOException ignored) {}
    }
}
