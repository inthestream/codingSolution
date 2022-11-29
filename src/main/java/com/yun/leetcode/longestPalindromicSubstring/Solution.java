package com.yun.leetcode.longestPalindromicSubstring;

import java.util.LinkedList;
import java.util.List;

public class Solution {
    // not solved yet
    public static void main(String[] args) {
        String s = "aet232tecb12344321b";
        //lengthOfPalindromeString(s, 3, 5);
        int max = 0;
        String maxStringIndex = "";

        List<String> indecis = findSmallCases(s);

        for (int i = 0; i < indecis.size(); i++) {
            int len = lengthOfPalindromeString(s, Integer.valueOf(indecis.get(i).split(",")[0]), Integer.valueOf(indecis.get(i).split(",")[1]));

            if (len > max) {
                max = len;
                maxStringIndex = indecis.get(i);
            }
        }

        System.out.println(maxStringIndex);
    }



    /**
     * Returns indecies of all small cases
     *
     * @param s Palindrome string
     * @return indecies list as concatenated with start and end index
     */
    static List<String> findSmallCases(String s) {
        List<String> indecies = new LinkedList<>();
        for (int i = 0; i < s.length() - 2; i++) {
            if (s.charAt(i) == s.charAt(i+2)) {
                indecies.add(i + "," + (i+2));
                i += 2;
            } else if (s.charAt(i) == s.charAt(i+1)) {
                indecies.add(i + "," + (i+1));
                i++;
            }
        }

        if (s.charAt(s.length()-1) == s.charAt(s.length()-2)) {
            indecies.add((s.length()-2) + "," + (s.length()-1));
        }

        return indecies;
    }

    /**
     * return length of palindrome string from small cases
     *
     * @param s Palindrome string
     * @param start start index of small case of palindrome
     * @param end end index of small case of palindrome
     * @throws NullPointerException if {@code s} is null
     */
    static int lengthOfPalindromeString(String s, int start, int end) {
        if (start >= end || s.trim().equals("")) return 0;
        if (s == null) throw new NullPointerException("String is null.");

        int length = 0;

        int fromIndex = start;
        int lastIndex = end;

        while (s.charAt(--fromIndex) == s.charAt(++lastIndex)) {
            if (fromIndex > 0 && lastIndex < (s.length()-1)) break;

        }

        length = (lastIndex-1) - (fromIndex+1) + 1;

        return length;
    }
}
