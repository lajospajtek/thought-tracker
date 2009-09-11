package cso;

/**
 * Not a very good design. Rethink.
 */
public class ExecutionTime {

    private long start;
    private long stop;
    private byte[] buf;

    public void start() {
        start = System.nanoTime();
    }

    public void stop() {
        stop = System.nanoTime();
    }

    public void setBuf(byte[] buf) {
        this.buf = buf;
    }

    public long get() {
        return stop - start;
    }

    public long getStart() {
        return start;
    }

    public long getStop() {
        return stop;
    }
}
