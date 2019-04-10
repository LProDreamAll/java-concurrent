package lhh.day04.producerandconsumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class MoneyConsumer implements Runnable {

    private BlockingQueue<Money> queue;
    private static final int SLEEPTIME = 1000;

    public MoneyConsumer(BlockingQueue<Money> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("start consumer id = " + Thread.currentThread().getId());
        Random r = new Random();
        try {
            while (true) {
                Money data = queue.take();
                if (null != data) {
                    int i = data.getIntData() * data.getIntData();
                    System.out.println("计算平方值："+i);
                    Thread.sleep(r.nextInt(SLEEPTIME));
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
