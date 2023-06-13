package Server.Concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Pool {
    ThreadPoolExecutor executorService;
    private static int minThreads = 5;
    private static int maxThreads = 15;
    private static int keepAliveSeconds = 60;
    private BlockingQueue<Runnable> tickets;
    private Pool () {
        tickets = new LinkedBlockingDeque<>();
        executorService = new ThreadPoolExecutor(minThreads, maxThreads, keepAliveSeconds, TimeUnit.SECONDS, tickets);
    }
    private static Pool instance = null;

    private static Pool getInstance () {
        if (instance == null) instance = new Pool();
        return instance;
    }

    public static void newThread (Runnable run) {
        getInstance().executorService.execute(run);
    }

}
