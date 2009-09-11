import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.Deque;
import java.util.ArrayDeque;

/**
 * See: http://page.mi.fu-berlin.de/prechelt/phonecode/
 * and: http://page.mi.fu-berlin.de/prechelt/phonecode/taskdescription.html
 */
public class Phonecode {

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

    private String[] rawDict = new String[75000];
    private int dictSize;
    private String[] numberDict = new String[75000];
    private PhoneNumbers numbers;
    private long initDt;

    public Phonecode(String dictName, String numberListName) {
        init(dictName);
        numbers = new PhoneNumbers(numberListName);
    }

    private void go() {
        long t0 = System.nanoTime();
        String number;
        while ((number = numbers.next()) != null) {
            match(number);
        }
        numbers.close();
        long t1 = System.nanoTime() - t0;
        System.err.println("init  :" + initDt/1000000.0 + " ms");
        System.err.println("search:" + t1/1000000.0 + " ms");
        System.err.println("total :" + (initDt + t1)/1000000.0 + " ms");

    }

    private void init(String dictName) {
        long t0 = System.nanoTime();
        try {
            BufferedReader in = new BufferedReader(new FileReader(dictName));
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
        initDt = System.nanoTime() - t0;
    }

    private void match(String number) {
        new Matcher(number).go();
    }

    StringBuilder sb = new StringBuilder(50);
    private String toNumbers(String str) {
        sb.setLength(0);
        for (int i = 0; i < str.length(); ++i) {
            char c = toNumber(Character.toLowerCase(str.charAt(i)));
            if (c != 0) sb.append(c);
        }
        return sb.toString();
    }

    private char toNumber(char c) {
        return (char) translator[c];
    }

    class Matcher {
        String number;
        String raw;
        int numLen;

        Matcher(String number) {
            this.number = cleanup(number);
            numLen = this.number.length();
            raw = number;
        }

        void go() {
            go(0, true);
        }

        Deque<String> solution = new ArrayDeque<String>();

        void go(int idx, boolean allowDigit) {
            if (idx == numLen) {
                System.out.print(raw + ":");
                for (String element : solution) {
                    System.out.print(" " + element);
                }
                System.out.println();
                return;
            }
            String sub = number.substring(idx);
            for (int i = 0; i < dictSize; ++i) {
                if (sub.startsWith(numberDict[i])) {
                //if (startsWithAtOffset(number, idx, numberDict[i]) == 0) {
                    allowDigit = false;
                    solution.addLast(rawDict[i]);
                    go(idx + numberDict[i].length(), true);
                    solution.removeLast();
                    //System.out.println(number + ": " + rawDict[i]);
                }
            }
            // binary search
            /*int pos = dictSize/2;
            int delta = pos/2;
            while (true) {
                String str = rawDict[pos];
                for (int i=idx, j=0; i<numLen; ++i, ++j) {
                    int c = number.charAt(i) - str.charAt(j);
                    if (c == 0) continue;
                    if (c < 0) {
                        
                    }
                }
                break;
            }*/
            if (allowDigit) {
                for (char c = '0'; c <= '9'; ++c) {
                    if (number.charAt(idx) == c) {
                        solution.addLast("" + c);
                        go(idx + 1, false);
                        solution.removeLast();
                    }
                }
            }
        }

        private int startsWithAtOffset(String str1, int offset, String str2) {
            int n = str1.length()-offset;
            int p1 = offset;
            int p2 = 0;
            if ( n < str2.length()) return 1;
            n = Math.min(n, str2.length());
            while (n-- > 0) {
                int c = str1.charAt(p1++) - str2.charAt(p2++);
                if (c != 0) return c;
            }
            return 0;
        }

        private StringBuilder sb = new StringBuilder(50);
        private String cleanup(String line) {
            sb.setLength(0);
            if (line == null) return null;
            for (int i = 0; i < line.length(); ++i) {
                char c = line.charAt(i);
                if (Character.isDigit(c)) {
                    sb.append(c);
                }
            }
            //System.out.println("## " + line + " >> " + sb);
            return sb.toString();
        }
    }

    class PhoneNumbers {
        public BufferedReader nin;

        public PhoneNumbers(String numberListName) {
            try {
                nin = new BufferedReader(new FileReader(numberListName));
            } catch (IOException e) {
                System.out.println("ex:" + e);
                System.exit(1);
            }
        }

        public String next() {
            String line = null;
            try {
                line = nin.readLine();
            } catch (IOException e) {
                System.out.println("ex:" + e);
                System.exit(1);
            }
            return line;
        }

        public void close() {
            try {
                nin.close();
            } catch (IOException e) {
                System.out.println("ex:" + e);
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("two arguments required: <dictionary file> and <phone number list>");
            System.exit(1);
        }
        new Phonecode(args[0], args[1]).go();
    }
}
