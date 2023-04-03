package by.katz;

import java.util.concurrent.TimeUnit;

public class Timer extends Thread {

    private final ITimer target;
    private final Integer timeValue;
    private final TimeUnit timeUnit;
    private boolean timerStop = false;

    public Timer(ITimer target, TimeUnit timeUnit, Integer timeValue) {
        this.target = target;
        this.timeUnit = timeUnit;
        this.timeValue = timeValue;
    }

    @Override public synchronized void start() {
        super.start();
        System.out.println("Start timer! Interval: " + timeUnit.toString() + " " + timeValue);
    }

    @SuppressWarnings("unused") public void stopTimer() {
        System.out.println("Stop timer!");
        timerStop = true;
    }

    @SuppressWarnings("BusyWait") @Override public void run() {
        while (!timerStop) {
            try {
                Thread.sleep(timeUnit.toMillis(timeValue));
            } catch (InterruptedException e) {throw new RuntimeException(e);}
            target.onTimerEvent();
        }
    }

    interface ITimer {
        void onTimerEvent();
    }
}
