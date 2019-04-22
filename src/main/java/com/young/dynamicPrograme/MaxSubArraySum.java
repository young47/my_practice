package com.young.dynamicPrograme;

/**
 * 最大子数组的和
 */
public class MaxSubArraySum {
    private static int[] arrays = {-2, 1, -3, 4, -1, 2, 1, -5, 4};

    public static void main(String[] args) {
        System.out.println(findMaxSum());
        System.out.println(dpFind());
    }

    /**
     * S[i] = max(S[i-1]+a[i], a[i])
     * @return
     */
    private static int dpFind() {
        int[] dp = new int[arrays.length];
        dp[0] = arrays[0];
        int max = arrays[0];
        for (int i = 1; i < arrays.length; i++) {
            dp[i] = Math.max(dp[i-1] + arrays[i], arrays[i]);
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    private static int findMaxSum() {
        int sum = 0, max = 0;
        for (int array : arrays) {
            if (sum >= 0) {
                sum += array;
            } else {
                sum = array;
            }
            //System.out.println("sum="+sum);
            max = Math.max(max, sum);
        }
        return max;
    }
}

