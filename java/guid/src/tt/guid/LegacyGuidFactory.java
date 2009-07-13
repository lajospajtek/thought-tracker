package tt.guid;

import tt.guid.providers.RandomClockSequence;
import tt.guid.providers.NanoSequenceTime;
import tt.guid.providers.ClockSequenceProvider;
import tt.guid.providers.TimeProvider;

/**
 * Factory for time-based (type 1) UUIDs, as specified by RFC 4122.
 * Online available at: http://www.ietf.org/rfc/rfc4122.txt . There is one
 * deviation from the RFC, namely (per client request) the clock sequence is
 * not a sequence per se but a pseudo random number.
 *
 * @author <a href="lajos.pajtek@gmail.com">Lajos Pajtek</a>
 */
public class LegacyGuidFactory extends GuidFactory {

    private TimeProvider tp;

    private ClockSequenceProvider csp;

    /**
     * the node id (mac address)
     */
    private String node;

    /**
     * Constructor. Takes a 12 digit node id as argument, usually the MAC address of the
     * machine guid generator runs on.
     *
     * @param node the node id, must be exactly 12 char long
     */
    public LegacyGuidFactory(String node) {
        if (node == null) {
            String msg = "LegacyGuidFactory: the MAC address cannot be null.";
            throw new IllegalArgumentException(msg);
        }
        if (node.length() != 12) {
            String msg = "LegacyGuidFactory: the MAC address must have exactly 12 cahracters. The provided string \""
                    + node + "\" has " + node.length() + "characters.";
            throw new IllegalArgumentException(msg);
        }
        this.node = node;
        tp = new NanoSequenceTime();
        csp = new RandomClockSequence();
    }

    /**
     * Generates a globally unique identifier for the URL processor.
     * @return the guid
     */
    public String getGuid() {
        String time = toHexString(getMsb(tp.getTime()));
        // the clock sequence multiplexed with the variant
        String clockseq = Integer.toHexString(0x8000 | csp.getClockSequence());
        //String guid = time + "-" + clockseq + "-" + node;
        String guid = time + clockseq + node;
        return guid;
    }

    /**
     * Performs the reordering of the bits and inserts the version number.
     * (type 1, time based, bits: 0001.)
     *
     * @param hNanoTime the timestamp computed according to the RFC
     * @return the reshufled most significant bits
     */
    private long getMsb(long hNanoTime) {
        long msb = (hNanoTime & 0xffffffffL) << 32; // time low
        long hiMedTime = hNanoTime >>> 32;
        msb |= (hiMedTime & 0xffffL) << 16; // time mid
        msb |= 0x1000; // version bits; this is the "time based version"
        msb |= (hiMedTime & 0x0fff0000L) >>> 16; // time hi
        return msb;
    }
}
