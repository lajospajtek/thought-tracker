package cso;

import java.security.MessageDigest;
import java.util.concurrent.Callable;
import java.util.Random;

/**
 */
public class Md5Computation implements Callable<ExecutionTime> {

    private final MessageDigest md;
    private final byte[] buf;

    public Md5Computation(final byte[] buf) throws Exception {
        this.buf = buf;
        md = MessageDigest.getInstance("SHA");
    }

    public ExecutionTime call() {
        ExecutionTime t = new ExecutionTime();
        t.start();
        md.update(buf);
        byte[] digest = md.digest();
        t.setBuf(digest); // save a reference to prevent garbage collection
        t.stop();
        return t;
    }
}
