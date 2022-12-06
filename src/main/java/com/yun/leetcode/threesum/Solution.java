package com.yun.leetcode.threesum;

import java.util.*;

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

        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i : nums) {
            map.put(i, i);
        }

        int leftIdx = 0;
        int rightIdx = nums.length-1;
        int dupLeftInt = Integer.MIN_VALUE;
        int dupRightInt = Integer.MAX_VALUE;

        int leftZeroIdx = 0;
        int rightZeroIdx = nums.length-1;

        while (nums[leftZeroIdx] != 0) {
            leftZeroIdx++;
        }

        while (nums[rightZeroIdx] != 0) {
            rightZeroIdx--;
        }

        while (leftIdx < rightIdx) {
            int twosum = nums[leftIdx] + nums[rightIdx];

            if (nums[leftIdx] == dupLeftInt) {
                leftIdx++;
                continue;
            } else if (nums[rightIdx] == dupRightInt){
                rightIdx--;
                continue;
            }

            if (binarySearch(nums, leftIdx+1, rightIdx-1, twosum*-1) > -1) {
                result.add(Arrays.asList(nums[leftIdx] ,twosum*(-1) ,nums[rightIdx]));
            }



            if (twosum == 0) {

              if (leftZeroIdx - leftIdx < rightIdx - rightZeroIdx) {
                  dupRightInt = nums[rightIdx];
                  rightIdx--;
              } else {
                  dupLeftInt = nums[leftIdx];
                  leftIdx++;
              }
            } else if (twosum > 0) {
                dupRightInt = nums[rightIdx];
                rightIdx--;
            } else {
                dupLeftInt = nums[leftIdx];
                leftIdx++;
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

        int[] nums = {-1,0,1,2,-1,-4,-2,-3,3,0,4};
        System.out.println(s.threeSum(nums).size());

    }
}