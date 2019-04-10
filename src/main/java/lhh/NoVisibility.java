package lhh;


public class NoVisibility {
    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {
        @Override
        public void run() {
            while (!ready)
                Thread.yield();
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        while (true) {
            new ReaderThread().start();
            new ReaderThread().start();
            number = 42;
            ready = true;
            Thread.yield();//当前线程让出CPU、异步日志的处理
        }

    }
}
