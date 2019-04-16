package lhh.day06.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Div implements Runnable {
    public static BlockingQueue<Msg> bq = new LinkedBlockingQueue<>();

    @Override
    public void run() {
        while (true) {
            try {
                Msg msg = bq.take();
                msg.i = msg.i / 2;
                System.out.println(msg.orjStr+"="+msg.i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
