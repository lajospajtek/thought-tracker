package tt.guid.providers;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class IncrementalClockSequence implements ClockSequenceProvider {

    private static AtomicInteger clockSeq;

    static {
        Random r = new Random();
        clockSeq = new AtomicInteger(r.nextInt(0x2000));
    }

    public int getClockSequence() {
        return clockSeq.incrementAndGet() & 0x1ffff;
    }
}
