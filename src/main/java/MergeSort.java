package main.java;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class multithreaded sorts random-generated array by MergeSort
 * @author Maksim Baranov
 * @since 1.8
 */

public class MergeSort {

    private static final int ARRAY_SIZE_FOR_SOLO_WORKING = 10;
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

        public MyRecursiveAction(int[] arr) {
            this.arr = arr;
        }

        @Override
        public void compute() {
            if (arr.length < 2) {
                return;
            }
            int mid = arr.length >>> 1;
            int[] arr1 = Arrays.copyOfRange(arr, 0, mid);
            int[] arr2 = Arrays.copyOfRange(arr, mid, arr.length);
            if (arr.length < ARRAY_SIZE_FOR_SOLO_WORKING) {
                sortMerge(arr);
                sortMerge(arr);
            } else {
                invokeAll(new MyRecursiveAction(arr1), new MyRecursiveAction(arr2));
                merge(arr, arr1, arr2);
            }
        }

        public void sortMerge(int[] arr) {
            if (arr.length < 2) {
                return;
            }
            int mid = arr.length >>> 1;
            int[] arr1 = Arrays.copyOfRange(arr, 0, mid);
            int[] arr2 = Arrays.copyOfRange(arr, mid, arr.length);
            sortMerge(arr1);
            sortMerge(arr2);
            merge(arr, arr1, arr2);
        }

        private void merge(int[] arr, int[] arr1, int[] arr2) {
            int i = 0, j = 0, k = 0;
            while (i < arr1.length && j < arr2.length) {
                if (arr1[i] <= arr2[j]) {
                    arr[k++] = arr1[i++];
                } else {
                    arr[k++] = arr2[j++];
                }
            }
            while (i < arr1.length) {
                arr[k++] = arr1[i++];
            }
            while (j < arr2.length) {
                arr[k++] = arr2[j++];
            }
        }
    }
}

