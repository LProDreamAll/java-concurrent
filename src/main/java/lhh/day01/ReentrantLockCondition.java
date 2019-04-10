package lhh.day01;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class ReentrantLockCondition implements Runnable {

    public static ReentrantLock lock = new ReentrantLock();

    public static Condition condition = lock.newCondition();

    @Override
    public void run() {
        try {
            lock.lock();
            condition.await(); //java.util.concurrent.locks.AbstractQueuedSynchronizer.ConditionObject.await()
            System.out.println("Thread is going on ");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockCondition rlc = new ReentrantLockCondition();
        Thread thread = new Thread(rlc);
        thread.start();
        Thread.sleep(2000);
        lock.lock();
        //唤醒才会结束
        condition.signal();
        lock.unlock();
    }
}
