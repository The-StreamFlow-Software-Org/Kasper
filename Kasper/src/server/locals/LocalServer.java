package server.locals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalServer {

    public static String timestampNow() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
