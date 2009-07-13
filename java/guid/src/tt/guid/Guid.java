package tt.guid;

/**
 * Unused currently.
 */
public class Guid {

    protected final long msb;
    protected final long lsb;

    /**
     *
     * @param msb most significant word
     * @param lsb least significant word
     */
    public Guid(long msb, long lsb) {
        this.msb = msb;
        this.lsb = lsb;
    }
}
