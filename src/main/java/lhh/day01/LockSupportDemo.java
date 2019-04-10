package lhh.day01;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport 的静态方法可以堵塞park()可以堵塞当前线程
 */
public class LockSupportDemo {
    public static Object u = new Object();
    static ChangeObjectThread c1 = new ChangeObjectThread("t1");
    static ChangeObjectThread c2 = new ChangeObjectThread("t2");
    public static class ChangeObjectThread extends Thread{
        public ChangeObjectThread(String name ){
            super.setName(name);
        }

        @Override
        public void run() {
            synchronized (u){
                System.out.println("in "+getName());
                LockSupport.park();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        c1.start();
        TimeUnit.SECONDS.sleep(1);
        c2.start();
        LockSupport.unpark(c1);
        LockSupport.unpark(c2);
        c1.join();
        c2.join();
    }
}
