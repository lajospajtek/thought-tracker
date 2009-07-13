package cso;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 */
public class CsoExecutor {

    private static final int BUFSIZ = 10240;
    private static final Random r = new Random();

    private final ExecutorService es;
    private final int num;

    public CsoExecutor(ExecutorService e, int n) {
        es = e;
        num = n;
    }

    public void execute(String prefix) throws Exception {
        List<Md5Computation> computationList = new ArrayList<Md5Computation>(num);
        for (int i=0; i< num; ++i) {
            byte[] buf = new byte[BUFSIZ];
            r.nextBytes(buf);
            computationList.add(new Md5Computation(buf));
        }
        long t0 = System.nanoTime();
        List<Future<ExecutionTime>> futureList = es.invokeAll(computationList);
        es.shutdown();
        long t1 = System.nanoTime();
        MinMax mm = new MinMax(futureList);
        System.out.println(prefix + " total: " + ((t1-t0)/1000000.0) + " (" + ((mm.getDelta())/1000000.0) + ")");
    }   
}
