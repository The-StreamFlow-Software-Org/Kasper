package KasperCommons.Network;

public class Timer {
        private long startTime;
        private long stopTime;
        private boolean running;

        public void start() {
            if (!running) {
                startTime = System.currentTimeMillis();
                running = true;
            } else {
                reset();
            }
        }

        public double stop() {
            if (running) {
                stopTime = System.currentTimeMillis();
                running = false;
                long elapsedTime = stopTime - startTime;
                return elapsedTime / 1000.0; // Convert milliseconds to seconds (double precision)
            }
            return 0.0;
        }

        public void reset() {
            startTime = 0;
            stopTime = 0;
            running = false;
        }


    private static Timer timer;

    public static Timer getTimer() {
        if (timer == null) {
            timer = new Timer();
        } return timer;

    }



}
