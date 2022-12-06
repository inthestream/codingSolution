package com.yun.leetcode.integerToRoman;

/**
 Runtime: 32 ms, faster than 6.64% of Java online submissions for Integer to Roman.
 Memory Usage: 48.3 MB, less than 9.37% of Java online submissions for Integer to Roman.
 */
public class Solution {
    final String[] symbol =         {"M"    , "D"   , "C"   , "L"   , "X"   , "V"   , "I"};
    final Integer[] symbolValue =   {1000   , 500   , 100   , 50    , 10    , 5     , 1};

    public String intToRoman(int num) {
        int number = num;

        String result = "";

        for (int i = 0; number > 0 && i < symbolValue.length; i++) {
            int divisor = symbolValue[i];
            int divided = number / divisor;

            if (divided > 0) {
                if (i % 2 == 0) {
                    if (divided == 4) {
                        result += symbol[i] + symbol[i - 1];
                    } else {
                        result += loopCharacter(symbol[i], divided);
                    }
                } else {
                    if (String.valueOf(number).startsWith("9")) {
                        result += symbol[i+1] + symbol[i - 1];
                        number -= (divisor / 5) * Integer.valueOf(String.valueOf(number).charAt(0)+"");
                        i++;
                        continue;
                    } else {
                        result += loopCharacter(symbol[i], divided);
                    }

                }
            }

            number -= (divided * divisor);
        }

        return result;
    }

    private String loopCharacter(String c, int count) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        System.out.println(s.intToRoman(59));
    }
}
