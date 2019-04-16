package lhh.day07;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.StampedLock;

/**
 * Unsafe.park()遇到异常会直接返回。
 * StampedLock内部实现时，使用类似CAS操作的死循环反复尝试的策略，
 * 并没有处理有关中断的逻辑，这就会导致堵塞在park()上的线程被中断后，
 * 会再次进入循环，当退出条件不满足的时候会疯狂的占用cpu
 * CLH:自旋锁
 * 锁维护一个等待线程队列，所有申请锁，但是没有成功的线程都记录在这个队列中。
 * 每一个节点，一个节点代表一个线程，保存一个标记位，locked
 * while(pred.locked){}判断前序节点是否已经成功释放锁
 */
public class StampedLockCPUDemo {

    static Thread[] threads = new Thread[3];

    static final StampedLock lock = new StampedLock();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> { //此处开启线程占用writeLock锁
            long readLong = lock.writeLock();
            //并不释放锁一直等待
            LockSupport.parkNanos(600000000000L);
            lock.unlockWrite(readLong);
        }).start();

        Thread.sleep(100);
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new HoldCPUReadThread());
            threads[i].start();//此时开启线程请求读锁，所有线程都会被最终挂起
        }
        //等待10s后
        Thread.sleep(10000);
        //线程中断后会占用CPU 飙升
        for (int i = 0; i < 3; i++) {
            threads[i].interrupt();
        }

    }

    private static class HoldCPUReadThread implements Runnable {
        @Override
        public void run() {
            long lockr = lock.readLock();
            System.out.println(Thread.currentThread().getName() + " 获得锁");
            lock.unlockRead(lockr);
        }
    }
}
