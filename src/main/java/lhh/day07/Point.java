package lhh.day07;

import java.util.concurrent.locks.StampedLock;

public class Point {

    private double x, y;

    private final StampedLock s1 = new StampedLock();

    void move(double deltaX, double deltaY) {
        long stamp = s1.writeLock();//这是一个排它锁
        try {
            x += deltaX;
            y += deltaY;
        } catch (Exception e) {
            s1.unlockWrite(stamp);
        }
    }

    double distanceFromOrigin() {//只读方法
        long stamp = s1.tryOptimisticRead();
        double currentX = x, currentY = y;//无法确定此时x=y
        if (!s1.validate(stamp)) {//判断stamp在读的过程中是否被修改
            stamp = s1.readLock();
            try {
                currentX = x;
                currentY = y;
            } catch (Exception e) {
                s1.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * x + currentY * y);

    }
}
