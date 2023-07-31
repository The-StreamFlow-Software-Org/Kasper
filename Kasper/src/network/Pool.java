package network;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Pool {
    ThreadPoolExecutor executorService;
    private static int minThreads = 20;
    private static int maxThreads = 120;
    private static int keepAliveSeconds = 60;
    private BlockingQueue<Runnable> tickets;
    private Pool () {
        tickets = new LinkedBlockingDeque<>();
        executorService = new ThreadPoolExecutor(minThreads, maxThreads, keepAliveSeconds, TimeUnit.SECONDS, tickets, new CustomThreadFactory());
    }

    class CustomThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable runnable) {
            try {
                var thisRunnable = runnable;
                Thread thread = new Thread(runnable);
               // thread.setUncaughtExceptionHandler(new CustomUncaughtExceptionHandler(thisRunnable));
                return thread;
            } catch (ClassCastException e) {
                return new Thread(runnable);
            }
        }
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

    public static ExecutorService get () {return getInstance().executorService;}
    public static void reset () {
        shutdown();
        Thread thread = new Thread(()-> {
            while (!instance.executorService.isShutdown()){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } getInstance().executorService = null;
            instance = new Pool();
        });
        thread.start();

    }
}
