package lhh.day06.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 没有太理解
 */
public class Utils {


    static int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};//可以使用静态代码块
    static ExecutorService pool = Executors.newCachedThreadPool();
    static final int THREAD_NUM = 2;
    static AtomicInteger result = new AtomicInteger(-1);

    public static int search(int searchValue, int beginPos, int endPos) {
        int j = 0;
        for (int i = beginPos; i < endPos; i++) {
            if (result.get() >= 0) {
                return result.get();
            }
            if (arr[i] == searchValue) {
                //设置返回值，如果设置失败则代表别的线程已经得到
                if (!result.compareAndSet(-1, i)) {
                    return result.get();
                }
                return i;
            }
        }
        return -1;
    }

    public static class SearchTask implements Callable<Integer> {

        int begin, end, searchValue;

        public SearchTask(int begin, int end, int searchValue) {
            this.begin = begin;
            this.end = end;
            this.searchValue = searchValue;
        }

        @Override
        public Integer call() throws Exception {
            return search(searchValue, begin, end);
        }
    }

    public static int pSearch(int searchValue) throws ExecutionException, InterruptedException {
        int subArrSize = arr.length / THREAD_NUM + 1;
        List<Future<Integer>> re = new ArrayList<>();
        for (int i = 0; i < arr.length; i += subArrSize) {
            int end = i + subArrSize;
            if (end >= arr.length) end = arr.length;
            re.add(pool.submit(new SearchTask(searchValue, i, end)));
        }
        for (Future<Integer> integerFuture : re) {
            if (integerFuture.get() >= 0) {
                return integerFuture.get();
            }
        }
        return -1;
    }


}
