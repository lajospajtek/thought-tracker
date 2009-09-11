package tt.guid;

/**
 */
public abstract class GuidFactory {

    public abstract String getGuid();

    protected static String addLeadingZeros(String number, int span) {
        if (number == null) {
            return null;
        }
        int zeros = span - number.length();
        if (zeros == 0) {
            return number; // nothing to do
        } else if (zeros < 0) {
            return number.substring(0, span); // arbitrary decision...
        }
        StringBuffer sb = new StringBuffer(span);
        while (zeros-- > 0) {
            sb.append('0');
        }
        sb.append(number);
        return sb.toString();
    }

    protected static String toHexString(short n) {
        return addLeadingZeros(Integer.toHexString(n), 4);
    }

    protected static String toHexString(int n) {
        return addLeadingZeros(Integer.toHexString(n), 8);
    }

    protected static String toHexString(long n) {
        return addLeadingZeros(Long.toHexString(n), 16);
    }
}
