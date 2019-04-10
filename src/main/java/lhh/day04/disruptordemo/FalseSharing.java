package lhh.day04.disruptordemo;

/**
 * 处理CPU缓存CPU cache line 占据同一行的时候将不会命中
 */
public class FalseSharing implements Runnable {

    private final static int NUM_THREADS = 2;


    private final static long ITERATIONS = 550L * 1000L * 1000L;

    private final int arrayIndex;

    private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];

    static {
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
    }

    public FalseSharing(final int arrayIndex) {
        this.arrayIndex = arrayIndex;

    }


    public final static class VolatileLong {
        public volatile long value = 0L;
//        public long p1, p2, p3, p4, p5, p6, p7;//用于把各个value分开，防止他们进入同一个缓存
    }


    @Override
    public void run() {
        long l = ITERATIONS + 1;
        while (0 != --l) {
            longs[arrayIndex].value = l;
        }
    }

    public static void main(String[] args) throws Exception {
        final long start = System.currentTimeMillis();
        runTest();
        System.out.println("duration = " + (System.currentTimeMillis() - start));
    }

    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

    }
}
