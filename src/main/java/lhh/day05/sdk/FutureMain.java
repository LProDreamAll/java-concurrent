package lhh.day05.sdk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FutureMain {

    public static void main(String[] args) throws Exception {
        FutureTask<String> future = new FutureTask<>(new RealData("ah"));
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(future);
        System.out.println("请求完毕");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("数据=" + future.get());
    }
}
