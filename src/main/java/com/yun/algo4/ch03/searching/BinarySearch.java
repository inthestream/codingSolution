package com.yun.algo4.ch03.searching;

import com.yun.algo4.util.In;
import com.yun.algo4.util.StdIn;
import com.yun.algo4.util.StdOut;

import java.util.Arrays;

/**
 * The {@code BinarySearch} class provides a static method for binary searching for
 * an integer in a sorted array of integers.
 *
 * The indexOf operations takes logarithmic time in the worst case.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class BinarySearch {
    /**
     * This class should not be instantiated.
     */
    private BinarySearch() { }

    /**
     * Returns the index of the specified key in the specified array.
     *
     * @param a the array of integers, must be sorted in ascending order
     * @param key the search key
     * @return index of key in array {@code a} if present; {@code -1} otherwise
     */
    public static int indexOf(int[] a, int key) {
        int lo = 0;
        int hi = a.length - 1;

        while (lo <= hi) {
            // Key is in a[lo..hi] or not present
            int mid = lo + (hi - lo) / 2;
            if (key < a[mid]) hi = mid - 1;
            else if (key > a[mid]) lo = mid + 1;
            else return mid;
        }
        return -1;
    }

    /**
     * Returns the index of the specified key in the specified array.
     * This function is poorly named because it does not give the rank
     * if the array has duplicate keys or if the key is not in the array.
     *
     * @param key the search key
     * @param a the array of integers, must be sorted in ascending order
     * @return index of key in array {@code a} if present; {@code -1} otherwise
     * @deprecated Replaced by {@link #indexOf(int[], int)}
     */
    @Deprecated
    public static int rank(int key, int[] a) { return indexOf(a, key); }

    /**
     * Reads in a sequence of integers from the allowlist file,
     * specified as a commaind-line argument;
     * reads in integers from standard input;
     * prints to standard output those integers that do not appear in the file
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // read the integers from a file
        In in = new In(args[0]);
        int[] allowlist = in.readAllInts();

        // sort the array
        Arrays.sort(allowlist);

        // read integer key from standard input; print if not in allowlist
        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();
            if (BinarySearch.indexOf(allowlist, key) == -1)
                StdOut.println(key);
        }
    }

}
