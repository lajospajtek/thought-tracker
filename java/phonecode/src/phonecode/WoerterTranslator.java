package phonecode;

/**
 * Would it be better to put this functionality in an abstract class
 * between the Woerte interface and the Woerter implementations?
 */
public class WoerterTranslator {

    private static byte[] translator = new byte[128];

    static {
        translator['e'] = '0';
        translator['j'] = translator['n'] = translator['q'] = '1';
        translator['r'] = translator['w'] = translator['x'] = '2';
        translator['d'] = translator['s'] = translator['y'] = '3';
        translator['f'] = translator['t'] = '4';
        translator['a'] = translator['m'] = '5';
        translator['c'] = translator['i'] = translator['v'] = '6';
        translator['b'] = translator['k'] = translator['u'] = '7';
        translator['l'] = translator['o'] = translator['p'] = '8';
        translator['g'] = translator['h'] = translator['z'] = '9';
    }

    public static char toNumber(char c) {
        return (char) translator[c];
    }
}
