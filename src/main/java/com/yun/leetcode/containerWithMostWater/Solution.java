package com.yun.leetcode.containerWithMostWater;

/**
 * Runtime: 19 ms, faster than 21.38% of Java online submissions for Container With Most Water.
 * Memory Usage: 81 MB, less than 46.33% of Java online submissions for Container With Most Water.
 */

public class Solution {
    public int maxArea(Integer[] height) {
        int i = 0, j = height.length-1;
        int maxArea = 0;
        while (i < j) {
            int h = Math.min(height[i], height[j]);
            int w = j - i;

            maxArea = Math.max(h*w, maxArea);

            if (height[i] < height[j]) i++;
            else j--;
        }
        return maxArea;
    }

    public static void main(String[] args) {
        Solution s = new Solution();
        Integer[] a = {1,8,6,2,5,4,8,3,7};

        System.out.println(s.maxArea(a));
    }
}


