package phonecode;

import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;

/**
 */
public class Solutions {

    private List<String> solutions = new ArrayList<String>();

    public Solutions() {
    }

    public void add(String sol) {
        solutions.add(sol);
    }

    public void printSolutions(PrintStream ps) {
        for (String sol : solutions) {
            ps.println(sol);
        }
    }

    public void printSolutions() {
        printSolutions(System.out);
    }
}
