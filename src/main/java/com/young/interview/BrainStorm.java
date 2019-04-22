package com.young.interview;

/**
 * 头脑暴风写代码
 */
public class BrainStorm {
    public static int[] sortedArr = {1, 3, 3, 5, 7, 9, 9};

    public static int[] target = {3, 9, 2, 1, 3, 1, 4, 2, 1, 6, 5, 7, 5, 5};

    public static void main(String[] args) {
        /*int[] arr = Sort.generateArrays(1000);
        selectionSort(arr);
        insertSort(arr);
        System.out.println(Sort.checkSorted(arr));
        System.out.println(binarySearch(sortedArr, 1, 0, sortedArr.length - 1));*/

        longestAscendingSeqLength(target);
        longestAscendingSeq(target);
        longestAscendingStr(target);
    }

    public static void selectionSort(int[] arr) {
        for (int i = 0; i <= arr.length - 1; i++) {
            int min = arr[i];
            for (int j = i; j <= arr.length - 1; j++) {
                if (arr[j] < min) {
                    min = arr[j];
                }
            }
            arr[i] = min;
        }
    }

    public static void insertSort(int[] arr) {
        for (int i = 0; i <= arr.length - 1; i++) {
            int value = arr[i];
            int j = i - 1;
            for (; j >= 0; j--) {
                if (arr[j] > value) {
                    arr[j + 1] = arr[j];
                } else {
                    break;
                }
            }
            arr[j + 1] = value;
        }
    }

    public static int binarySearch(int[] arr, int target, int begin, int end) {
        while (begin <= end) {
            int middle = (begin + end) / 2;
            if (arr[middle] <= target) {
                begin = middle + 1;
            } else {
                end = middle - 1;
            }
        }
        return begin;
    }

    /**
     * 子数组的最大和
     * <p>
     * sum(j) = max(sum(j-1) + arr[j], arr[j])
     *
     * @param arr
     * @return
     */
    public static int maxSubArrSum(int[] arr) {
        int[] sums = new int[arr.length];
        sums[0] = arr[0];
        int max = arr[0];
        for (int j = 1; j < arr.length - 1; j++) {
            sums[j] = Math.max(sums[j - 1] + arr[j], arr[j]);
            max = Math.max(max, sums[j]);
        }
        return max;
    }

    /**
     * 最长不下降子序列长度
     * <p>
     * L(i) = max(L(j)) + 1, j<i & arr[j]<=arr[i]
     *
     * @param arr
     * @return
     */
    public static int longestAscendingSeqLength(int[] arr) {
        int[] L = new int[arr.length];
        int longest = 1;
        for (int i = 0; i < L.length; i++) {
            L[i] = 1;
        }
        for (int i = 0; i <= arr.length - 1; i++) {
            for (int j = 0; j <= i - 1; j++) {
                if (arr[j] <= arr[i]) {
                    L[i] = Math.max(L[j] + 1, L[i]);
                }
            }
            longest = Math.max(longest, L[i]);
        }
        System.out.println("longestAscendingSeqLength = " + longest);
        return longest;
    }

    /**
     * 最长不下降子序列
     * LAS(j) = longest(LAS(i)).concat(arr[j]), i<j & arr[i]<=arr[j]
     *
     * @param arr
     * @return length
     */
    public static int longestAscendingSeq(int[] arr) {
        String[] LAS = new String[arr.length];
        for (int i = 0; i < LAS.length; i++) {
            LAS[i] = arr[i] + "";
        }
        String longestSeq = "";
        for (int j = 0; j < arr.length; j++) {
            for (int i = 0; i < j; i++) {
                if (arr[i] <= arr[j]) {
                    LAS[j] = longest(LAS[i] + arr[j], LAS[j]);
                }
            }
            longestSeq = longest(longestSeq, LAS[j]);
        }
        System.out.println("longestAscendingSeq=" + longestSeq);
        return longestSeq.length();
    }

    /**
     * 最长不下降子串
     * LAS(j) = LAS(j-1), if arr[j]>=arr[j-1]
     *
     * @param arr
     */
    public static void longestAscendingStr(int[] arr) {
        String longestStr = "";
        for (int i = 0; i < arr.length; i++) {
            String iLongestStr = arr[i] + "";
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] >= arr[j - 1]) {
                    iLongestStr += arr[j];
                }else{
                    break;
                }
            }
            longestStr = longest(longestStr, iLongestStr);
        }
        System.out.println("longestAscendingStr=" + longestStr);
    }

    private static String longest(String s, String s1) {
        return s.length() >= s1.length() ? s : s1;
    }
}
