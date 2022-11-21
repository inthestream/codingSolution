package com.yun.algo4.ch03.searching;

import com.yun.algo4.util.StdIn;
import com.yun.algo4.util.StdOut;

/**
 *  symbol-table client that finds the number of occurrences of each string
 *  in a sequence of strings from standard input,
 *  then iterates through the keys to find the one that occurs the most frequently.
 */
public class FrequencyCounter {
    public static void main(String[] args) {
        int minlen = Integer.parseInt(args[0]);
        SequentialSearchST<String, Integer> st = new SequentialSearchST<String, Integer>();
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            if (word.length() < minlen) continue;
            if (!st.contains(word)) st.put(word, 1);
            else                    st.put(word, st.get(word) + 1);
        }

        String max = "";
        st.put(max, 0);
        for (String word : st.keys())
            if (st.get(word) > st.get(max))
                max = word;

        StdOut.println(max + " " + st.get(max));
    }
}
