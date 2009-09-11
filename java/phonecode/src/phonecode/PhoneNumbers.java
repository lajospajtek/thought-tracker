package phonecode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 */
public class PhoneNumbers {
    
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
