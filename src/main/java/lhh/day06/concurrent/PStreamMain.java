package lhh.day06.concurrent;

public class PStreamMain {
    public static void main(String[] args) {
        new Thread(new Plus()).start();
        new Thread(new Multiply()).start();
        new Thread(new Div()).start();

        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                Msg msg = new Msg();
                msg.i = i;
                msg.j = j;
                msg.orjStr = "((" + i + "+" + j + ")*" + i + ")/2";
                Plus.bq.add(msg);//把数据提交给加法线程
            }
        }
    }
}
