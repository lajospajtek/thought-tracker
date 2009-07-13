package phonecode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 */
public class Woerter1 implements Woerter {

    public static final int MAXSIZ = 75000;

    private String[] rawDict = new String[MAXSIZ];
    private int dictSize;
    private String[] numberDict = new String[MAXSIZ];

    public Woerter1(String filename) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            int i = 0;
            String str;
            while ((str = in.readLine()) != null) {
                rawDict[i] = str;
                numberDict[i] = toNumbers(str);
                //System.out.println(rawDict[i] + "-> " + numberDict[i]);
                ++i;
            }
            dictSize = i;
            in.close();
        } catch (IOException e) {
            System.out.println("ex:" + e);
            System.exit(1);
        }
    }

    public int getDictSize() {
        return dictSize;
    }

    public String getNumber(int i) {
        return numberDict[i];
    }

    public Pair getPair(int i) {
        throw new UnsupportedOperationException("not implemented");
    }

    public String getRaw(int i) {
        return rawDict[i];
    }

    private String toNumbers(String str) {
        StringBuilder sb = new StringBuilder(50);
        for (int i = 0; i < str.length(); ++i) {
            char c = WoerterTranslator.toNumber(Character.toLowerCase(str.charAt(i)));
            if (c != 0) sb.append(c);
        }
        return sb.toString();
    }
}
