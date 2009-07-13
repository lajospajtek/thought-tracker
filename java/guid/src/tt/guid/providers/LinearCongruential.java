package tt.guid.providers;

/**
 */
public class LinearCongruential {

    private int next;
    private int multiplier;
    private int increment;
    private int mask;

    /**
     *
     * @param multiplier
     * @param increment
     * @param bits
     */
    public LinearCongruential(int multiplier, int increment, int bits) {
        this.multiplier = multiplier;
        this.increment = increment;
        this.mask = (1 << bits) - 1;
        this.next = ((int)System.currentTimeMillis()) & mask;
    }

    /**
     *
     */
    public LinearCongruential() {
        this(0xE66D, 0xB, 16);
    }

    /**
     *
     * @return the next pseudo random number (?)
     */
    synchronized public int next() {
        next = (next * multiplier + increment) & mask;
        return next;
    }
}
// 0x5DEECE66DL