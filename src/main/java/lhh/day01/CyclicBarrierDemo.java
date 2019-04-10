package lhh.day01;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class CyclicBarrierDemo {
    public static class Soldier implements Runnable {

        private String Soldier;

        private final CyclicBarrier cyclicBarrier;

        public Soldier(String soldierName, CyclicBarrier cyclicBarrier) {
            Soldier = soldierName;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            //等待所有士兵到齐
            try {
                cyclicBarrier.await();
                doWork();
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        private void doWork() {
            try {
                TimeUnit.SECONDS.sleep(Math.abs(new Random().nextInt() % 10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Soldier + "任务完成！");
        }
    }

    public static class BarrierRun implements Runnable {

        boolean flag;
        int N;

        public BarrierRun(boolean flag, int N) {
            this.flag = flag;
            this.N = N;
        }


        @Override
        public void run() {
            if (flag) {
                System.out.println(N + "个士兵完成任务！");
            } else {
                System.out.println(N + "个士兵集合完毕！");
                flag = true;
            }
        }
    }

    public static void main(String[] args) {
        final int N = 10;
        Thread[] allSoldier = new Thread[N];
        boolean flag = false;
        CyclicBarrier cyclic = new CyclicBarrier(N, new BarrierRun(flag, N));
        System.out.println("集合队伍");
        for (int i = 0; i < N; i++) {
            System.out.println("士兵" + i + "报道！");
            allSoldier[i] = new Thread(new Soldier("士兵" + i, cyclic));
            allSoldier[i].start();
            //9 个 BrokenBarrierException 1 个 InterruptedException
            //其他9个线程将不会等待会抛出 9 个 BrokenBarrierException
            if (i == 5) {
                allSoldier[0].interrupt();//这个线程抛出InterruptedException
            }
        }

    }
}
