package com.yun.leetcode.threesum;

import java.util.*;

/**
 * Runtime 246 ms Beats 37.1%
 * Memory 46.4 MB Beats 92.44%
 */

class Solution {
    /**
     * Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]] such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.
     * Notice that the solution set must not contain duplicate triplets.
     * @param nums
     * @return
     */
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new LinkedList<>();
        if (nums.length < 3) return result;
        Arrays.sort(nums);

        if (nums[0] > 0) return result;
        if (nums[nums.length-1] < 0) return result;

        int dupLeftInt = Integer.MIN_VALUE;
        int dupRightInt;

        for (int i = 0; i < nums.length && nums[i] <= 0; i++) {
            if (dupLeftInt == nums[i]) {
                continue;
            } else {
                dupLeftInt = nums[i];
            }

            dupRightInt = Integer.MAX_VALUE;
            for (int j = i+1; j < nums.length && nums[i] + nums[j] <= 0; j++) {
                if (dupRightInt == nums[j]) {
                    continue;
                } else {
                    dupRightInt = nums[j];
                }

                int twosum = nums[i] + nums[j];

                if (binarySearch(nums, j+1, nums.length-1, twosum * -1) > -1) {
                    result.add(Arrays.asList(nums[i], nums[j], twosum * (-1)));
                }
            }
        }
        return result;
    }


    private int binarySearch(int[] a, int start, int end, int s) {
        int lo = start;
        int hi = end;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (s < a[mid]) hi = mid - 1;
            else if (s > a[mid]) lo = mid + 1;
            else return mid;
        }
        return -1;
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        int[] nums0 = {0,0,0};

        System.out.println(s.threeSum(nums0).size());

        int[] nums = {-1,0,1,2,-1,-4,-2,-3,3,0,4};

        //System.out.println(s.threeSum(nums).size());

        int[] nums2 = {-1,0,1,2,-1,-4};

        //System.out.println(s.threeSum(nums2).size());

        int[] nums3 = {-2,0,1,1,2};

        System.out.println(s.threeSum(nums3).size());

        int[] nums4 = {-4,-2,-2,-2,0,1,2,2,2,3,3,4,4,6,6};

        System.out.println(s.threeSum(nums4).size());


    }
}