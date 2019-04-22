package com.young.dynamicPrograme;

/**
 * 注意 子串和子序列的区别
 */

public class LongestAscendingStr {
    private static String s = "39213142165755";
    //private static String s = "329415";
    //private static String s = "494278";

    public static void main(String[] args) {
        System.out.println("不下降子串:" + longestAscendingStr());
        longestAscendingSeqLength();
        System.out.println("不下降子序列:" + longestAscendingSeq());
    }


    /**
     * 最长不下降子串
     * dp[i][j]表示从[i, j]之间的子串是否为不下降子串
     *
     * @return
     */
    private static String longestAscendingStr() {
        String longest = "";
        int length = s.length();
        boolean[][] dp = new boolean[length][length];

        for (int j = 0; j <= length - 1; j++) {
            for (int i = 0; i <= j; i++) {
                if (i == j) {
                    dp[i][j] = true;
                } else if ((j - i) == 1) {
                    dp[i][j] = s.charAt(i) <= s.charAt(j);
                } else if ((j - i) >= 2) {
                    dp[i][j] = s.charAt(j - 1) <= s.charAt(j) && s.charAt(i) <= s.charAt(i + 1) && dp[i + 1][j - 1];
                }
                if (dp[i][j] && (j - i + 1) > longest.length()) {
                    longest = s.substring(i, j + 1);
                }
            }
        }
        return longest;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void longestAscendingSeqLength() {
        traverse();
        dp();
        method3();
    }

    /**
     * 遍历所有情况
     */
    private static int tmp = 1;

    private static void traverse() {
        int cl = 1;//当前的长度
        int longest = 1;
        for (int i = 0; i < s.length(); i++) {
            //f(i, 1);
            forward(s.charAt(i), i + 1, cl);
            longest = Math.max(longest, tmp);
        }
        System.out.println("遍历：最大不下降子序列长度=" + tmp);
    }

    /**
     * f(i, cl)表示从第i个元素开始的子序列，其中最大不下降子序列的长度
     *
     * @param i
     * @param cl
     */
    private static void f(int i, int cl) {
        if (i >= s.length() - 1) {
            return;
        }
        //找第一个不小于s[i]的值
        for (int j = i + 1; j < s.length(); j++) {
            int cll = cl;
            if (s.charAt(i) <= s.charAt(j)) {
                cll++;
                for (int k = j + 1; k < s.length(); k++) {
                    if (s.charAt(j) <= s.charAt(k) && s.charAt(k) >= s.charAt(k - 1)) {
                        cll++;
                    }
                }
                //f(j, cl + 1);
            }
            tmp = Math.max(tmp, cll);
        }
    }

    //找第i个元素
    private static void forward(char lastChar, int i, int cl) {
        if (i > s.length() - 1) {
            if (cl > tmp){
                tmp = cl;
            }
            return;
        }
        //跳过第i个元素
        forward(lastChar, i + 1, cl);
        //第i个元素包含在内
        if (lastChar <= s.charAt(i)) {
            forward(s.charAt(i), i + 1, cl + 1);
        }
    }


    /**
     * 动态规划
     * <p>
     * d(j)表示以s[j]结尾的不下降子序列的长度
     * 则 d(j) = max[d(i)]+1 当且仅当 i<j s[i]<=s[j]
     * <p>
     * 对于第j个元素结尾的最大不下降子序列，就是在j之前的j-1个元素中查找那个最大的不下降子序列(因为可能有多个不下降子序列，比如3，4，5，1，2，6. 6之前就有两个不下降子序列)
     * <p>
     * 由代码可看出 时间复杂度为O(n^2)
     */
    private static void dp() {
        int longest = 1;
        int[] d = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            d[i] = 1; //都初始化为1，最大不下降子序列的最小长度为1
        }
        for (int j = 1; j < s.length(); j++) {
            for (int i = 0; i < j; i++) {//去j之前查找最大的不下降子序列
                if (s.charAt(i) <= s.charAt(j)) {
                    d[j] = Math.max(d[j], d[i] + 1); //这句话的意思是，每在j之前找到一个不下降子序列，都要和当前的d[j]进行比较
                }
            }
            //走到这里，d[j]已经确定
            longest = Math.max(longest, d[j]);
        }
        System.out.println("动态规划：最大不下降子序列长度=" + longest);
    }

    /**
     * 遍历数组维护一个单调递增的数组
     * 这种方法只适合计算length
     */
    private static void method3() {
        char[] d = new char[s.length()];
        d[0] = s.charAt(0);
        int max = 1;
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (d[max - 1] <= c) {
                d[max++] = c;
            } else {
                //在[0 ... i-1]中找到第一个大于c的
                for (int j = 0; j <= max - 1; j++) {
                    if (d[j] > c) {
                        d[j] = c;
                        break;
                    }
                }
                //这里也可以用二分查找
                /*int left = 0, right = max - 1;
                while (left <= right) {
                    int mid = (left + right) / 2;
                    if (c <= d[mid]) {
                        right = mid - 1;
                    } else {
                        left = mid + 1;
                    }
                }
                d[++right] = c;*/
            }
            /*System.out.print("d=");
            for (int k = 0; k < max; k++) {
                System.out.print(d[k]);
            }
            System.out.println("");*/
        }

        System.out.println("第三种方法：最大不下降子序列长度=" + max);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * LAS(j)表示第j个元素P结尾的LAS，则
     * LAS(j) = longest(LAS(i))+P
     *
     * @return
     */
    private static String longestAscendingSeq() {
        String[] LAS = new String[s.length()];
        String longest = "";
        for (int j = 0; j < s.length(); j++) {
            for (int i = 0; i < j; i++) {
                if (s.charAt(i) <= s.charAt(j)) {//加=，就是不降低序列；不加=，就是上升序列
                    LAS[j] = longest(LAS[i] + s.charAt(j), LAS[j]);
                }
            }
            if (LAS[j] == null) {//前i个都比自己大
                LAS[j] = String.valueOf(s.charAt(j));//是自己
            }
            longest = longest(longest, LAS[j]);
            //System.out.println("LAS[" + j + "]=" + LAS[j]);
        }
        return longest;
    }

    private static String longest(String s1, String s2) {
        if (s1 == null) {
            return s2;
        }
        if (s2 == null) {
            return s1;
        }
        return s1.length() >= s2.length() ? s1 : s2;
    }
}
