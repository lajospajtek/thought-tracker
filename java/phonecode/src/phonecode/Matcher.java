package phonecode;

import java.util.Deque;
import java.util.ArrayDeque;

class Matcher {

    private static Solutions solutions = new Solutions();
    private Woerter w;
    private String number;
    private String raw;
    private int numLen;
    private Deque<String> solution = new ArrayDeque<String>();

    Matcher(Woerter w, String number) {
        this.w = w;
        this.number = cleanup(number);
        numLen = this.number.length();
        raw = number;
    }

    void match() {
        match(0, true);
    }

    private void match(int idx, boolean allowDigit) {
        if (idx == numLen) {
            buildSolutions();
            return;
        }
        boolean f = false;
        String sub = number.substring(idx);
        int dictSize = w.getDictSize();
        // TODO: put a binary search here for the first letter; backtrack to the previous letter
        // run the 'startsWith(number)' loop, break if advanced to the next letter
        for (int i = 0; i < dictSize; ++i) {
            Pair pair = w.getPair(i);
            String number = pair.key;
            //if (sub.charAt(0) < number.charAt(0)) continue;
            //if (sub.charAt(0) > number.charAt(0)) break;
            if (sub.startsWith(number)) {
                f = true;
                //if (startsWithAtOffset(number, idx, numberDict[i]) == 0) {
                allowDigit = false;
                // TODO this loop seems to add some redundancy. FIXME
                for (String raw: pair.values) {
                    solution.addLast(raw);
                    match(idx + number.length(), true);
                    solution.removeLast();
                }
                //System.out.println(number + ": " + rawDict[i]);
            } else if (f) break;
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
                    match(idx + 1, false);
                    solution.removeLast();
                }
            }
        }
    }

    /*        private int startsWithAtOffset(String str1, int offset, String str2) {
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
    */

    private void buildSolutions() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(raw).append(":");
        for (String element : solution) {
            sb.append(" ").append(element);
        }
        solutions.add(sb.toString());        
    }

    private String cleanup(String line) {
        if (line == null) return null;
        StringBuilder sb = new StringBuilder(50);
        for (int i = 0; i < line.length(); ++i) {
            char c = line.charAt(i);
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        //System.out.println("## " + line + " >> " + sb);
        return sb.toString();
    }

    public static void printSolutions() {
        solutions.printSolutions();
    }
}
