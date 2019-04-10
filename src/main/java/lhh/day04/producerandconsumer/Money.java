package lhh.day04.producerandconsumer;

public class Money {

    private final int intData;

    public Money(int andIncrement) {
        this.intData = andIncrement;
    }

    public Money(String d) {
        this.intData = Integer.valueOf(d);
    }

    public int getIntData() {
        return intData;
    }

    @Override
    public String toString() {
        return "data:" + intData;
    }
}
