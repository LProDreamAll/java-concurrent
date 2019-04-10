package lhh.day04.producerandconsumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MoneyProducer implements Runnable {
    private volatile boolean isRunning = true;
    private BlockingQueue<Money> queue;
    private static AtomicInteger count = new AtomicInteger();
    private static final int SLEEPTIME = 1000;

    public MoneyProducer(BlockingQueue<Money> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Money ma = null;
        Random r = new Random();
        System.out.println("start producer id = " + Thread.currentThread().getId());
        try {
            while (isRunning) {
                //这里的作用只是为了结果看起来 生产消费像同一批次的
                Thread.sleep(r.nextInt(SLEEPTIME));
                ma = new Money(count.getAndIncrement());
                System.out.println(ma + "is put into queue");
                if (!queue.offer(ma, 2, TimeUnit.SECONDS)) {//提交到数据缓冲区 两秒钟限制
                    System.err.println("failed to put ma:" + ma);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            //清除标记
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        isRunning = false;
    }
}
