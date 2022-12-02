package com.yun.leetcode.zigzagConversion;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows like this:
 * (you may want to display this pattern in a fixed font for better legibility)
 * P   A   H   N
 * A P L S I I G
 * Y   I   R
 * And then read line by line: "PAHNAPLSIIGYIR"
 *
 * Runtime: 22 ms, faster than 43.25% of Java online submissions for Zigzag Conversion.
 * Memory Usage: 43.2 MB, less than 84.02% of Java online submissions for Zigzag Conversion.
 */

public class Solution {
    public String convert(String s, int numRows) {
        if (s == null || s.trim().length() == 0) throw new IllegalArgumentException("");

        if (numRows == 1) return s;

        StringBuilder sb = new StringBuilder();
        int between = (numRows - 1) * 2;
        int c = s.length();

        if (s.length() < between + 1) {
            while (c != between + 1) {
                s += "@";
                c++;
            }
        }

        if ((s.length() % between - 1) != 0) {
            while ((c % between - 1) != 0) {
                s += "@";
                c++;
            }
        }

        Stack<Integer> bottom = new Stack<>();

        for (int i = numRows-1; i < s.length(); i += between) {
            bottom.push(i);
        }

        List<Integer> firstRowNums = new LinkedList<>();
        for (int i = 0; i < s.length(); i += between) {
            firstRowNums.add(i);
        }

        LinkedList<Integer> list = new LinkedList<>();

        while (!bottom.isEmpty()) {
            int i = bottom.pop();

            if (i + 1 < s.length() && !list.contains(i + 1)) list.add(i + 1);
            if (!list.contains(i - 1)) list.add(i - 1);

            sb.append(s.charAt(i));
        }

        boolean plusMinus = true;

        while (!list.isEmpty()) {
            int i = list.poll();

            if (firstRowNums.contains(i)) {
                sb.append(s.charAt(i));
                continue;
            }

            if (plusMinus) {
                if (i + 1 < s.length() && !list.contains(i + 1)) list.add(i + 1);
            }
            else {
                if (!list.contains(i - 1)) list.add(i - 1);
            }

            plusMinus = plusMinus == true ? false : true;

            sb.append(s.charAt(i));
        }

        return sb.reverse().toString().replace("@","");
    }

    /**
     * Unit test
     *
     * @param args
     */
    public static void main(String[] args) {
        Solution s = new Solution();
        System.out.println(s.convert("abcd", 2));
    }



    public String convertSolution(String s, int nRows) {
        char[] c = s.toCharArray();
        int len = c.length;
        StringBuffer[] sb = new StringBuffer[nRows];
        for (int i = 0; i < sb.length; i++) sb[i] = new StringBuffer();

        int i = 0;
        while (i < len) {
            for (int idx = 0; idx < nRows && i < len; idx++) // vertically down
                sb[idx].append(c[i++]);
            for (int idx = nRows-2; idx >= 1 && i < len; idx--) // obliquely up
                sb[idx].append(c[i++]);
        }
        for (int idx = 1; idx < sb.length; idx++)
            sb[0].append(sb[idx]);
        return sb[0].toString();
    }
}


