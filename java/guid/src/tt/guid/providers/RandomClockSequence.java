package tt.guid.providers;

import java.util.Random;

public class RandomClockSequence implements ClockSequenceProvider {

    private static Random r = new Random();

    public int getClockSequence() {
        return r.nextInt(0x2000);
    }
}
