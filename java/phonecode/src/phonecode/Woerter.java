package phonecode;

/**
 */
public interface Woerter {

    @Deprecated
    String getRaw(int i);

    int getDictSize();

    @Deprecated
    String getNumber(int i);

    Pair getPair(int i);
}
