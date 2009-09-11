package phonecode;

/**
 */
public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Phonecode: two arguments required: <dictionary file> and <phone number list>");
            System.exit(1);
        }
        new Phonecode(args[0], args[1]).go();
    }
}
