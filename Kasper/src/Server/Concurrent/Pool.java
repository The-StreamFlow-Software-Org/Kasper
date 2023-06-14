package Server.Concurrent;

import java.util.ArrayList;
import java.util.concurrent.*;

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
    public static void invokeAll(ArrayList<Callable<Void>> runnables) throws InterruptedException {
        getInstance().executorService.invokeAll(runnables);
    }

    public static void shutdown () {
        getInstance().executorService.shutdown();
    }

}
