package lhh.day07;

import java.util.Random;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * Striped64是 LongAccumulator 和 LongAdder的父类，
 * LongAccumulator功能较多，都是把long进行分割放到不同的变量中
 */
//获取10000线程中
public class LongAccumulatorDemo {
    public static void main(String[] args) throws InterruptedException {
        //采用java8传递行为 采用Long中的max方法
        long l = System.currentTimeMillis();
        LongAccumulator longAccumulator = new LongAccumulator(Long::max, Long.MIN_VALUE);
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; i++) {
            //取到随机数的最大值
            threads[i] = new Thread(() -> {
                Random random = new Random();
                long value = random.nextLong();
                longAccumulator.accumulate(value);
            });
            threads[i].start();
        }
        for (int i = 0; i < 1000; i++) {
            threads[i].join();
        }
        System.out.println(longAccumulator.longValue());
        System.out.println((System.currentTimeMillis() - l) + " ms");
    }
}
