package application;

import java.util.Timer;
import java.util.TimerTask;

/**
 *Simple countdown timer demo of java.util.Timer facility.
 */

public class Countdown {
    public static void main(final String args[]) {

        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int i = 100;
            public void run() {
                System.out.println(i--);
                if (i< 0)
                    timer.cancel();
            }
        }, 0, 1000);
    }
}