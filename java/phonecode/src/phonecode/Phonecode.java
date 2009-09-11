package phonecode;

/**
 * See: http://page.mi.fu-berlin.de/prechelt/phonecode/
 * and: http://page.mi.fu-berlin.de/prechelt/phonecode/taskdescription.html
 */
public class Phonecode {

    private Woerter w;
    private PhoneNumbers numbers;
    private ExecutionTimer et = new ExecutionTimer();

    public Phonecode(String dictName, String numberListName) {
        et.start("init  ");
        w = new Woerter2(dictName);
        numbers = new PhoneNumbers(numberListName);
        et.stop();
    }

    public void go() {
        et.start("search");
        String number;
        while ((number = numbers.next()) != null) {
            match(w, number);
        }
        numbers.close();
        et.stop();
        et.printTimes();
        Matcher.printSolutions();
    }

    private void match(Woerter w, String number) {
        new Matcher(w, number).match();
    }
}