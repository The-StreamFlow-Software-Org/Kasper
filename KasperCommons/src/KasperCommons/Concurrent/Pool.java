package KasperCommons.Concurrent;

import KasperCommons.Network.KasperNitroWire;
import KasperCommons.Network.NetworkPackageRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Pool {
    ThreadPoolExecutor executorService;
    private static int minThreads = 5;
    private static int maxThreads = 30;
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
                var thisRunnable = (NetworkPackageRunnable) runnable;
                Thread thread = new Thread(runnable);
                thread.setUncaughtExceptionHandler(new CustomUncaughtExceptionHandler(thisRunnable.net));
                return thread;
            } catch (ClassCastException e) {
                return new Thread(runnable);
            }
        }
    }

    class CustomUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        KasperNitroWire network;
        public CustomUncaughtExceptionHandler (KasperNitroWire net){
            this.network = net;
        }
        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            try {
                network.put("Thrown by KasperEngine: Reason:> An internal exception occurred in the KasperEngine. Please contact your vendor for fixes.\nReason:> " + throwable.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static Pool instance = null;

    private static Pool getInstance () {
        if (instance == null) instance = new Pool();
        return instance;
    }

    public static void newThread (NetworkPackageRunnable run) {
        getInstance().executorService.execute(run);
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
