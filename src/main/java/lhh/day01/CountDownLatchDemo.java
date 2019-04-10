package lhh.day01;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * 倒计时器
 */
public class CountDownLatchDemo implements Runnable {

    public static final CountDownLatch end = new CountDownLatch(10);
    public static final CountDownLatchDemo demo = new CountDownLatchDemo();
    public static final AtomicInteger a = new AtomicInteger();

    @Override
    public void run() {

        try {
            TimeUnit.SECONDS.sleep(new Random().nextInt(10));
            System.out.println("check it");
            System.out.println("a: " + a.getAndIncrement());
            end.countDown();
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {

        ExecutorService es = newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            es.submit(demo);//堵塞主线程等待子线程完成任务
        }
        //等待检查
        end.await();
        System.out.println("fire!");
        es.shutdown();
    }
}
