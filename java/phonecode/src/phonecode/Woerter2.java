package phonecode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 */
public class Woerter2 implements Woerter {

    public static final int MAXSIZ = 75000;

    private Pair[] dictArray;
    private int dictSize;

    public Woerter2(String filename) {
        Map<String, Pair> dictMap = new HashMap<String, Pair>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String str;
            while ((str = in.readLine()) != null) {
                String cleared = toNumbers(str);
                Pair pair = dictMap.get(cleared);
                if (pair == null) {
                    pair = new Pair(cleared);
                    dictMap.put(cleared, pair);
                }
                pair.add(str);
            }
            dictSize = dictMap.size();
            if (dictSize > MAXSIZ) System.err.println("Warning: dictionary size larger than specified in the requirements: " + dictMap + " > " + MAXSIZ);
            in.close();
        } catch (IOException e) {
            System.out.println("ex:" + e);
            System.exit(1);
        }
        int i = 0;
        dictArray = new Pair[dictSize];
        for (Pair pair : dictMap.values()) {
            dictArray[i++] = pair;
            //System.out.println(". " + pair);
        }
        Arrays.sort(dictArray);
        for (int j = 0; j < dictSize; ++j) {
            //System.out.println("+ " + dictArray[j]);
        }
    }

    public int getDictSize() {
        return dictSize;
    }

    public String getNumber(int i) {
        return dictArray[i].key;
    }

    public String getRaw(int i) {
        return dictArray[i].values.get(0);
    }

    public Pair getPair(int i) {
        return dictArray[i];
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