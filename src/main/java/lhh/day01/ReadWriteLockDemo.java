package lhh.day01;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读读不堵塞
 * 读写堵塞
 * 写写堵塞
 *
 * 大概两秒多就能结束
 * 换成普通的锁就是20多秒
 */
public class ReadWriteLockDemo {
    public static ReentrantLock lock = new ReentrantLock();
    public static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    public static Lock readLock = readWriteLock.readLock();
    public static Lock writeLock = readWriteLock.writeLock();
    private int value;

    public Object handleRead(Lock lock) throws InterruptedException {
        try {
            lock.lock();
            TimeUnit.SECONDS.sleep(1);
            return value;
        } finally {
            lock.unlock();
        }
    }

    public void handleWrite(Lock lock, int index) throws InterruptedException {
        try {
            lock.lock();
            TimeUnit.SECONDS.sleep(1);
            value = index;
        } finally {
            lock.unlock();
        }

    }

    public static void main(String[] args) {
        ReadWriteLockDemo rwd = new ReadWriteLockDemo();
        Runnable readRunnable = () -> {
            try {
//                rwd.handleRead(readLock);
                rwd.handleRead(lock);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable writeRunnable = () -> {
            try {

//                rwd.handleWrite(writeLock,new Random().nextInt());
                rwd.handleWrite(lock,new Random().nextInt());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        //操作读
        for (int i = 0; i <18 ; i++) {
            new Thread(readRunnable).start();
        }

        //操作写
        for (int i = 18; i < 20; i++) {
            new Thread(writeRunnable).start();
        }


    }

}
