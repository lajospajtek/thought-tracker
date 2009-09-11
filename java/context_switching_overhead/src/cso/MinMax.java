package cso;

import java.util.concurrent.Future;
import java.util.List;

/**
 */
public class MinMax {

    long minStart = Long.MAX_VALUE, maxStop = 0;

    public MinMax(List<Future<ExecutionTime>> futureList) throws Exception {
        int num = futureList.size();
        for (int i=0; i< num; ++i) {
            ExecutionTime et = futureList.get(i).get();
            if (minStart > et.getStart()) minStart = et.getStart();
            if (maxStop < et.getStop()) maxStop = et.getStop();
        }
    }
    public long getDelta() {
        return maxStop - minStart;
    }
}
