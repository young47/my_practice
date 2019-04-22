package com.young.dynamicPrograme;

//动态规划求最长回文字段
public class LongestPalindrome {
    public static void main(String[] args) {
        String s = "abgbhjjhbgc";
        System.out.println(longestPalindrome(s));
        //
        //System.out.println(Math.random());
    }

    /**
     * dp[i][j] =
     * 1, true if i=j;
     * 2, str[i]==str[j] if j-i=1;
     * 3, str[i]==str[j] && dp[i+1][j-1] if j-i>=2;
     *
     * @param s
     * @return
     */
    private static String longestPalindrome(String s) {
        if (s == null) {
            return "";
        }
        int length = s.length();
        if (length <= 1) {
            return s;
        }
        boolean[][] dp = new boolean[length][length];
        String longest = "";
        for (int j = 0; j <= length - 1; j++) {
            for (int i = 0; i <= j; i++) {
                if (i == j) {
                    dp[i][j] = true;
                } else if (j - i == 1) {//相邻
                    dp[i][j] = (s.charAt(i) == s.charAt(j));
                } else if (j - i > 1) {
                    dp[i][j] = ((s.charAt(i) == s.charAt(j)) && dp[i + 1][j - 1]);
                }
                if (dp[i][j] && (j - i + 1) > longest.length()) {
                    longest = s.substring(i, j + 1);
                }
            }
        }
        return longest;
    }
}
