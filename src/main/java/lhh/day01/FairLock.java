package lhh.day01;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁的成本比较高性能比较低
 */
public class FairLock implements Runnable {

    public static ReentrantLock fairLock = new ReentrantLock(true);

    @Override
    public void run() {
        while (true) {
            try {
                fairLock.lock();
                System.out.println(Thread.currentThread().getName()+" 获得锁");
            } finally {
                fairLock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        FairLock r1 = new FairLock();
        Thread t1 = new Thread(r1, "t1");
        Thread t2 = new Thread(r1, "t2");
        t1.start();
        t2.start();
    }
}
