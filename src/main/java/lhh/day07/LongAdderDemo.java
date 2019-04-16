package lhh.day07;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 解决在高竞争情况下AtomicLong修改不成功导致性能受损的情况
 * 1：使用热点分离-将热点数据进行分解类似 ConcurrentHashMap
 * 把AtomicLong内部核心数据value分离成一个数组，通过哈希算法映射到其中一个数字进行计数、再把这个数组求和累加即可。
 * 2：LongAdder的操作
 * 1：内部实现一个变量base，在多线程情况下大家修改base都没有冲突，不会初始化新的数组出来。
 * 一旦出现冲突则会初始化新的cell数组，再有冲突，再次初始化新的cell数组。
 */
public class LongAdderDemo {
    //进行性能测试
    private static final int MAX_THREADS = 3;

    private static final int TASK_COUNT = 3;

    private static final int TARGET_COUNT = 10000000;

    private AtomicLong acount = new AtomicLong(0L); //无锁的原子操作

    private LongAdder lacount = new LongAdder();

    private long count = 0;

    static CountDownLatch cdlsync = new CountDownLatch(TASK_COUNT);

    static CountDownLatch cdlatomic = new CountDownLatch(TASK_COUNT);

    static CountDownLatch cdladdr = new CountDownLatch(TASK_COUNT);

    synchronized long inc() {
        return ++count;
    }

    synchronized long getCount() {
        return count;
    }


    public class SynThread implements Runnable {

        long startTime;
        LongAdderDemo out;

        @Override
        public void run() {
            long v = out.getCount();
            while (v < TARGET_COUNT) {
                v = out.inc();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("SynThread spend: " + (endTime - startTime) + "ms" + " v= " + v);
            cdlsync.countDown();
        }

        public SynThread(LongAdderDemo o, long startTime) {
            this.startTime = startTime;
            this.out = o;
        }
    }


    /**
     * 使用多线程进行基本类型累加
     *
     * @throws InterruptedException
     */
    public void testSync() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        long start = System.currentTimeMillis();
        SynThread synThread = new SynThread(this, start);
        for (int i = 0; i < TASK_COUNT; i++) {
            executor.submit(synThread);
        }
        cdlsync.await();
        executor.shutdown();
    }


    public class AtomicThread implements Runnable {

        long startTime;

        public AtomicThread(long startTime) {
            this.startTime = startTime;
        }

        @Override
        public void run() {
            long v = acount.get();
            while (v < TARGET_COUNT) {
                v = acount.incrementAndGet();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("AtomicThread spend: " + (endTime - startTime) + "ms" + " v= " + v);
            cdlatomic.countDown();
        }
    }


    /**
     * 使用多线程进行基本类型累加
     *
     * @throws InterruptedException
     */
    public void testAtomic() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        long start = System.currentTimeMillis();
        AtomicThread thread = new AtomicThread(start);
        for (int i = 0; i < TASK_COUNT; i++) {
            executor.submit(thread);
        }
        cdlatomic.await();
        executor.shutdown();
    }


    public class LongAdderThread implements Runnable {

        long startTime;

        public LongAdderThread(long startTime) {
            this.startTime = startTime;
        }

        @Override
        public void run() {
            long v = lacount.sum();
            while (v < TARGET_COUNT) {
                lacount.increment();
                v = lacount.sum();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("LongAdderThread spend: " + (endTime - startTime) + "ms" + " v= " + v);
            cdladdr.countDown();
        }
    }

    /**
     * 使用多线程进行LongAdder类型累加
     *
     * @throws InterruptedException
     */
    public void testLongAdder() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        long start = System.currentTimeMillis();
        LongAdderThread synThread = new LongAdderThread(start);
        for (int i = 0; i < TASK_COUNT; i++) {
            executor.submit(synThread);
        }
        cdladdr.await();
        executor.shutdown();
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        LongAdderDemo demo = new LongAdderDemo();
        demo.testSync();
        demo.testAtomic();
        demo.testLongAdder();

        /**
         * result:
         * SynThread spend: 1625ms v= 10000001
         * SynThread spend: 1625ms v= 10000000
         * AtomicThread spend: 667ms v= 10000000
         * AtomicThread spend: 667ms v= 10000002
         * AtomicThread spend: 667ms v= 10000001
         * LongAdderThread spend: 228ms v= 10000002
         * LongAdderThread spend: 228ms v= 10000002
         * LongAdderThread spend: 228ms v= 10000002
         *
         */
    }

}
