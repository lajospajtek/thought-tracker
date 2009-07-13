package cso;

import java.util.concurrent.Executors;

/**
 */
public class Main {

    public final static int NUM = 5000;

    public static void main(String[] args) throws Exception {
        Main m = new Main();
        m.go();
    }

    public void go() throws Exception {
        go(2); // warmup
        int[] threads = {100, 10, 2, 1, 100, 10, 2, 500, 999, 1};
        for (int n : threads) go(n);
    }

    void go(int n) throws Exception {
        if (n == 1) {
            CsoExecutor single = new CsoExecutor(Executors.newSingleThreadExecutor(), NUM);
            single.execute("single  \t");
        } else {
            CsoExecutor multi = new CsoExecutor(Executors.newFixedThreadPool(n), NUM);
            multi.execute("multi " + n + " \t");
        }
    }
}
