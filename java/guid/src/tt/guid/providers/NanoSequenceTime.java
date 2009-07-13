package tt.guid.providers;

import java.util.concurrent.atomic.AtomicInteger;

public class NanoSequenceTime implements TimeProvider {

    /**
     * a sequence number as surrogate for a 100 nanosecond granularity clock
     */
    private static AtomicInteger hNanoSequence = new AtomicInteger(0);

    /**
     * stores the previous system timestamp
     */
    private long previousMillis = 0L;

    /**
     * returns the time compoennt of a guid.
     * TODO: This method is not thread safe?
     * @return 
     */
    public long getTime() {
        long currentMillis = System.currentTimeMillis();
        if (previousMillis == currentMillis) {
            if (hNanoSequence.get() == 10000L) {
                hNanoSequence.set(0);
                while (currentMillis == System.currentTimeMillis()) {
                    // Stall untill the clock advances.
                    // Practically this case is extremely unlikely, but should ensure the correct
                    // behavior. Potential problems:
                    // 1. This limits the number of GUIDs that can be generated per time unnit, but it
                    // is quite unlikely that there will be ever a need to generate 10000 GUIDs within
                    // the system clock granularity (i.e. within 1 millisecond on Solaris,
                    // 10 milliseconds on Windows).
                    // 2. In the worst case could be a resource hog, but the worst case is practically
                    // unreachable even in performace test scenarios.
                    // 3. Stalls before returning the last valid guid. Should actually stall if no more
                    // guids are available within the current clock granularity.
                }
            }
        } else {
            previousMillis = currentMillis;
            hNanoSequence.set(0);
        }
        int hnseq = hNanoSequence.getAndIncrement();
        // The improbable number below is the offest between 15 Oct 1582 and 1 Jan 1970,
        // in 100 nanos. Ref. to the RFC for details.
        long hNanos = 0x01B21DD213814000L + currentMillis * 10000L + hnseq;
        // test:
        //long hNanos = currentMillis * 10000L + hnseq;
        //System.out.println(toHexString(currentMillis * 10000L) + ", " + hnseq);
        return hNanos;
    }
}
