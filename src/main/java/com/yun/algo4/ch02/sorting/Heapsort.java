package com.yun.algo4.ch02.sorting;


public class Heapsort {
    /**
     * Heapsort breaks into two phases: heap construction & sortdown
     *
     * @param a array which to be sorted
     */
    public static void sort(Comparable[] a) {
        int N = a.length;

        // heap construction
        for (int k = N/2; k >= 0; k--) {
            sink(a, k, N);
        }

        // sortdown
        while (N > 1) {
            exch(a, 1, N--);
            sink(a, 1, N);
        }
    }

    private static void sink(Comparable[] a, int k, int N) {
        while (k*2 < N) {
            int j = k*2;
            if (j < N && greater(a[j+1], a[j])) j++;
            if (greater(j, k)) break;
            exch(a, k, j);
            k = j;
        }
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static boolean greater(Comparable a, Comparable b) {
        return a.compareTo(b) > 0;
    }
}
