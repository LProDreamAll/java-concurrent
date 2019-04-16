package lhh.day07;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureDemo {
    public static Integer getc(Integer j) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return j * j;
    }

    public static Integer getc1(Integer j) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return j / 0;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> getc(50));
//        System.out.println(future.get());
//        chainCalls();//链式调用
        exceptionDemo();
    }

    /**
     * 异常处理
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void exceptionDemo() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> getc1(50))
                .exceptionally(e -> {
                    System.out.println(e.toString());
                    return 0;
                })
                .thenApply((i) -> Integer.toString(i))
                .thenApply((str) -> "\"" + str + "\"")
                .thenAccept(System.out::println);
        future.get();

    }

    /**
     * 链式调用
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void chainCalls() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> getc(50))
                .thenApply((i) -> Integer.toString(i))
                .thenApply((str) -> "\"" + str + "\"")
                .thenAccept(System.out::println);
        future.get();
    }
}
