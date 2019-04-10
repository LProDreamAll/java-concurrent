package lhh.day01;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 限时等待锁
 * 1：当尝试加锁的时间<sleep的时间，
 * 给下一个线程使用的时候
 * 将不会获取到锁，反之亦然。
 */
public class TimeLock implements Runnable {
    public static ReentrantLock lock = new ReentrantLock();
    @Override
    public void run() {
        try {
            //尝试锁住1s
            if (lock.tryLock(1, TimeUnit.SECONDS)) {
                TimeUnit.SECONDS.sleep(2);
            }else {

                System.out.println("get lock failed");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        TimeLock timeLock = new TimeLock();
        Thread t1 = new Thread(timeLock);
        Thread t2 = new Thread(timeLock);
        t1.start();
        t2.start();
    }
}
