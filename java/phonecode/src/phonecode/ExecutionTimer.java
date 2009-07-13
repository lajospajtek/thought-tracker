package phonecode;

import java.util.List;
import java.util.ArrayList;

/**
 * This is a hack.
 */
public class ExecutionTimer {

    private static final double MILLIS_IN_NANO = 1000000.0;

    private List<Ts> tstamps = new ArrayList<Ts>();
    private Ts currentTs;

    public void start(String name) {
        currentTs = new Ts(name);
    }

    public void stop() {
        currentTs.stop();
        tstamps.add(currentTs);
    }

    public void printTimes() {
        long delta = 0L;
        for (Ts stamp : tstamps) {
            delta += stamp.delta;
            System.err.println(stamp.name + " :" + stamp.delta/MILLIS_IN_NANO + " ms");
        }
        System.err.println("total :" + delta/MILLIS_IN_NANO + " ms");
        System.err.flush();
    }

    private class Ts {
        String name;
        long t0, t1, delta;
        Ts(String name) {
            this.name = name;
            t0 = System.nanoTime();
        }
        void stop() {
            t1 = System.nanoTime();
            delta = t1 - t0;
        }
    }
}
