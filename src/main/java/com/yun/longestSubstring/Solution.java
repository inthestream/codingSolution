package com.yun.longestSubstring;

import java.util.*;

public class Solution {
    public static void main(String[] args) {
       System.out.println(lengthOfLongestSubstring("ckilbkd"));
        System.out.println(lengthOfLongestSubstring("abba"));
        System.out.println(lengthOfLongestSubstring("dvdf"));
    }

    //my solution : it takes too long time
    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> map = new HashMap<>();

        int maxLen = 0;
        int start = 0;

        for (int i = 0; i < s.toCharArray().length; i++) {
            start = Math.max(start, map.getOrDefault(s.charAt(i), 0));

            maxLen = Math.max(maxLen, i - start + 1);

            map.put(s.charAt(i), i+1);
        }

        return maxLen;
    }


    public static int lengthOfLongestSubstring2(String s)
    {
        int maxLen = 0;

        // [1] longest substring is the one with the largest
        //    difference between positions of repeated characters;
        //    thus, we should create a storage for such positions
        int[] pos = new int[128];

        // [2] while iterating through the string (i.e., moving
        //    the end of the sliding window), we should also
        //    update the start of the window
        int start = 0, end = 0;

        for (char ch : s.toCharArray())
        {
            // [3] get the position for the start of sliding window
            //    with no other occurences of 'ch' in it
            start  = Math.max(start, pos[ch]);

            // [4] update maximum length
            maxLen = Math.max(maxLen, end-start+1);

            // [5] set the position to be used in [3] on next iterations
            pos[ch] = end + 1;

            end++;
        }

        return maxLen;
    }
}
