package tt.guid;

import java.util.UUID;

/**
 *
 */
public class RandomGuidFactory extends GuidFactory {

    public String getGuid() {
        UUID uuid = UUID.randomUUID();
        long hi = uuid.getMostSignificantBits();
        long lo = uuid.getLeastSignificantBits();
        String res = toHexString(hi) + toHexString(lo);
        System.out.println(res);
        System.out.println(uuid);
        return res;
    }

    public static void main(String[] args) {
        GuidFactory gf = new RandomGuidFactory();
        gf.getGuid();
    }
}
