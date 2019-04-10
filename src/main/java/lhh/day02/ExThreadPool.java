package lhh.day02;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ExThreadPool {
    public static class MyTask implements Runnable {
        public String name;

        public MyTask(String name) {
            this.name = name;
        }


        public void run() {
            log.info("正在执行" + ":Thread ID:" + Thread.currentThread().getId() + ",Task Name =" + name);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = new ThreadPoolExecutor(
                5,
                5,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10)) {
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                log.info("准备执行：" + ((MyTask) r).name);
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                log.info("执行完成：" + ((MyTask) r).name);
            }

            @Override
            protected void terminated() {
                log.info("线程池退出");
            }
        };

        for (int i = 0; i < 5; i++) {
            MyTask myTask = new MyTask("Task-geym-" + i);
            es.execute(myTask);
            Thread.sleep(10);
        }
        es.shutdown();

    }
}
