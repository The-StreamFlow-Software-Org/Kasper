package com.kasper.commons.Handlers;

import java.util.Timer;
import java.util.TimerTask;

public class CountdownTimer {

    private Timer timer;
    private long millisecondsRemaining;
    private TimerCallback callback;

    public CountdownTimer(long milliseconds, TimerCallback callback) {
        this.millisecondsRemaining = milliseconds;
        this.callback = callback;
    }

    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (millisecondsRemaining > 0) {
                    millisecondsRemaining -= 1000; // Decrease by 1 second (1000 ms)
                } else {
                    timer.cancel();
                    callback.onTimerFinished();
                }
            }
        }, 1000, 1000); // Start after 1 second and run every 1 second
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public interface TimerCallback {
        void onTimerFinished();
    }
}