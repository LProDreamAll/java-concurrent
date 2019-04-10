package lhh.day03;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 高并发下产生随机数的方案
 * 快贼快
 * 需要在看两遍
 */

/**
 * pool-1-thread-1: spend 2846ms
 * pool-1-thread-3: spend 2867ms
 * pool-1-thread-4: spend 2895ms
 * pool-1-thread-2: spend 2897ms
 * 多线程访问一个Random实例11505ms
 * pool-1-thread-4: spend 293ms
 * pool-1-thread-1: spend 300ms
 * pool-1-thread-2: spend 302ms
 * pool-1-thread-3: spend 308ms
 * 使用ThreadLocal包装实例1203ms
 */
public class MultiThreadDemo {

    private static final int GEN_COUNT = 10000000;

    private static final int THREAD_COUNT = 4;

    static ExecutorService es = Executors.newFixedThreadPool(THREAD_COUNT);

    static Random rnd = new Random(123);

    static ThreadLocal<Random> t = new ThreadLocal<Random>() {
        @Override
        protected Random initialValue() {
            return new Random(123);
        }
    };

    public static class RandTask implements Callable<Long> {
        private int mode = 0;

        public RandTask(int mode) {
            this.mode = mode;
        }

        public Random getRandom() {
            if (mode == 0) {//多线程共享一个Random
                return rnd;
            } else if (mode == 1) {//多线程各自分配一个Random
                return t.get();
            } else {
                return null;
            }

        }

        @Override
        public Long call() throws Exception {
            long l = System.currentTimeMillis();
            for (int i = 0; i < GEN_COUNT; i++) {
                getRandom().nextInt();
            }
            long l1 = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + ": spend " + (l1 - l) + "ms");
            return l1 - l;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<Long>[] futs = new Future[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            futs[i] = es.submit(new RandTask(0));
        }

        long totalTime = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            totalTime += futs[i].get();
        }
        System.out.println("多线程访问一个Random实例" + totalTime + "ms");

        //ThreadLocal
        for (int i = 0; i < THREAD_COUNT; i++) {
            futs[i] = es.submit(new RandTask(1));
        }

        totalTime = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            totalTime += futs[i].get();
        }
        System.out.println("使用ThreadLocal包装实例" + totalTime + "ms");
        es.shutdown();

    }

}
