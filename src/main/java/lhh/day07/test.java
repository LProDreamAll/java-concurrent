package lhh.day07;

import java.util.Arrays;

public class test {

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 54, 5};
        Arrays.parallelSort(arr);
        for (int i : arr) {
            System.out.println("i = " + i);
        }
    }
}
