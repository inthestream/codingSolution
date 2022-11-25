package com.yun.leetcode.longestPalindromicSubstring;

public class Solution {
    public static void main(String[] args) {
        longestPalindrome("abbac");
    }

    public static String longestPalindrome(String s) {
        char[] c = s.toCharArray();

        int fromIndex = 1;
        int midIndex = 0;
        int endIndex = 1;

        String result = "";

        while (fromIndex < c.length) {
            if (c[midIndex] != c[endIndex]) endIndex++;
            if (midIndex > 0 && c[midIndex] != c[endIndex]) midIndex++;

            fromIndex++;
        }

        for (int i = midIndex; i < endIndex; i++) {
            result += c[i];
        }

        return result;
    }
}
