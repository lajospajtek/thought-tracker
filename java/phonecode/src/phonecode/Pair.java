package phonecode;

import java.util.List;
import java.util.ArrayList;

class Pair implements Comparable<Pair> {
    public String key;
    public List<String> values = new ArrayList<String>();

    public Pair(String key) {
        this.key = key;
    }

    public void add(String value) {
        values.add(value);
    }
    
    @Override
    public int compareTo(Pair o) {
        return key.compareTo(o.key);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        sb.append(key).append("]->");
        for (String s : values) sb.append(s).append(',');
        return sb.toString();
    }
}