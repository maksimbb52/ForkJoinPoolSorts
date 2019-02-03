package main.java;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class multithreaded sorts random-generated array by QuickSort
 * @author Maksim Baranov
 * @since 1.8
 */

public class QuickSort {

    private static final int ARRAY_SIZE_FOR_SOLO_WORKING = 60;
    private static final int ARRAY_SIZE = 1000;
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 50;

    public static void main(String[] args) {
        int[] arr = ThreadLocalRandom.current().ints(ARRAY_SIZE, MIN_VALUE, MAX_VALUE).toArray();
        System.out.println(Arrays.toString(arr));
        ForkJoinPool.commonPool().invoke(new MyRecursiveAction(arr));
        System.out.println(Arrays.toString(arr));
    }

    private static class MyRecursiveAction extends RecursiveAction {

        private int[] arr;
        private int from;
        private int to;

        private static boolean flag = true;

        public MyRecursiveAction(int[] arr) {
            this(arr, 0, arr.length);
        }

        private MyRecursiveAction(int[] arr, int from, int to) {
            this.from = from;
            this.to = to;
            this.arr = arr;
        }

        @Override
        public void compute() {
            System.out.println(Thread.currentThread());
            if (to - from < 2) {
                return;
            }
            int pivotIndex = findPivotIndex(from, to);
            invokeAll(new MyRecursiveAction(arr, from, pivotIndex),
                    new MyRecursiveAction(arr, pivotIndex + 1, to));

        }

        private int findPivotIndex(int from, int to) {
            if (to - from < 2) {
                return from;
            }
            int pivot = arr[from];
            int i = from, j = from + 1;
            do {
                if (arr[j] <= pivot) {
                    ++i;
                    swapInArr(i, j);
                }
            } while (++j < to);
            swapInArr(from, i);
            if (from - to < ARRAY_SIZE_FOR_SOLO_WORKING) {
                findPivotIndex(from, i);
                findPivotIndex(i + 1, to);
            }
            return i;
        }

        private void swapInArr(int first, int second) {
            int temp = arr[first];
            arr[first] = arr[second];
            arr[second] = temp;
        }

    }

}
