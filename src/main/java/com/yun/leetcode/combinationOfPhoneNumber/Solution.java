package com.yun.leetcode.combinationOfPhoneNumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Solution {
    public List<String> letterCombinations(String digits) {
        List<String> ans = new ArrayList<>();
        HashMap<Character,String> map = new HashMap<>();
        map.put('2',"abc");
        map.put('3',"def");
        map.put('4',"ghi");
        map.put('5',"jkl");
        map.put('6',"mno");
        map.put('7',"pqrs");
        map.put('8',"tuv");
        map.put('9',"wxyz");

        for(int i = 0; i< digits.length(); i++){
            List<String> tmpNew = new ArrayList<>();
            String num = map.get(digits.charAt(i));
            for(int j = 0; j <num.length();j++){
                String newS = Character.toString(num.charAt(j));
                tmpNew.add(newS);
            }
            if(ans.size() != 0){
                List<String> tmpOld = ans;
                ans = new ArrayList<>();
                for(int j = 0; j< tmpOld.size(); j++){
                    for(int k = 0; k < tmpNew.size(); k++){
                        ans.add(tmpOld.get(j) + tmpNew.get(k));
                    }
                }
            }
            else{ans = tmpNew;}


        }
        return ans;
    }

    public static void main(String[] args) {
        Solution s = new Solution();
        s.letterCombinations("23");
    }
}
