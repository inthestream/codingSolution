package com.yun.leetcode.generateParentheses;

import java.util.*;

public class Solution {
    public List<String> generateParenthesis(int n) {
        Set<String> parenthese = new HashSet<>();
        if (n == 1) {
            parenthese.add("()");
            return new ArrayList<>(parenthese);
        }

        char[] p = new char[n*2];


        for (int i = 0; i < n; i++) {
            p[i] = '(';
            p[n*2 - i - 1] = ')';
        }

        for (int i = n; i > 0; i--) {
            int lIdx = i-1;
            int rIdx = lIdx+1;

            for (; rIdx < n*2-1; rIdx++, lIdx++) {

                moveToRight(p, rIdx);

                parenthese.add(convertToString(p));

                moveToRight(p, lIdx);

                parenthese.add(convertToString(p));
            }
        }

        return new ArrayList<>(parenthese);
    }




    public List<String> generateParenthesis0(int n) {
        Set<String> parenthese = new HashSet<>();

        String str = "()";
        parenthese.add(str);

        for (int i = 1; i < n; i++) {
            


        }


        return new ArrayList<>(parenthese);
    }

    private void moveToRight(char[] p, int idx) {
        char rightPart = p[idx+1];
        p[idx+1] = p[idx];
        p[idx] = rightPart;
    }

    private String convertToString(char[] p) {
        StringBuilder sb = new StringBuilder();

        for (char c : p) {
            sb.append(c);
        }

        return sb.toString();
    }


    public static void main(String[] args) {
        Solution s = new Solution();
        List aa = s.generateParenthesis(4);
        System.out.println(aa);
    }
}
