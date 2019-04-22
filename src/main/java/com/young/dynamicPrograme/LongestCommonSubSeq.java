package com.young.dynamicPrograme;

//最长公共子序列 (LCS)
public class LongestCommonSubSeq {
    private static int[] A = {2, 1, 9, 4, 3, 8, 7};
    private static int[] B = {3, 5, 1, 3, 8, 6, 2, 7};

    /**
     * 设An = [a1,a2,a3...an]
     * 设Bm = [b1,b2,b3...bm]
     * LCS[An, Bm]表示其LCS
     * L[An, Bm]表示其LCS的长度
     * 那么有：
     * L[An, Bm] =
     * 1, L[A(n-1), B(m-1)] + 1, if an = bm
     * 2, max(L[An, B(m-1)], L[A(n-1), Bm]), if an != bm
     * 初始值：
     * L[A1, B1] = 0;
     */
    public static void main(String[] args) {
        int[][] L = new int[A.length][B.length];
        System.out.println(findLengthOfLCS(A, B, L));
        findLCS(A, B, L);
    }

    private static int findLengthOfLCS(int[] a, int[] b, int[][] L) {
        int max = 1;
        //填充第一行
        for (int i = 0; i < b.length; i++) {
            if (a[0] == b[i]) {
                L[0][i] = 1;
                for (int j = i + 1; j < b.length; j++) {
                    L[0][j] = 1;
                }
                break;
            }
        }
        //填充第一列
        for (int i = 0; i < a.length; i++) {
            if (b[0] == a[i]) {
                L[i][0] = 1;
                for (int j = i + 1; j < a.length; j++) {
                    L[j][0] = 1;
                }
                break;
            }
        }
        for (int i = 1; i < a.length; i++) {
            for (int j = 1; j < b.length; j++) {
                if (a[i] == b[j]) {
                    L[i][j] = L[i - 1][j - 1] + 1;
                } else {
                    L[i][j] = Math.max(L[i - 1][j], L[i][j - 1]);
                }
                max = Math.max(max, L[i][j]);
            }
        }
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < a.length; j++) {
                System.out.print(L[j][i] + ",");
            }
            System.out.println(" ");
        }
        return max;
    }

    private static void findLCS(int[] a, int[] b, int[][] arrays) {
        int[] indexs = new int[4];//4是L
        int id = indexs.length - 1;
        outer:
        for (int i = arrays.length - 1; i >= 0; ) {//行数
            for (int j = arrays[i].length - 1; j >= 0; ) { //列数
                if (a[i] != b[j]) {
                    if ((i - 1) >= 0 && (j - 1) >= 0) {
                        if (arrays[i - 1][j] > arrays[i][j-1]) {
                            i--;
                        } else {
                            j--;
                        }
                    } else {
                        break outer;
                    }
                } else {
                    indexs[id--] = i;
                    i--;
                    j--;
                    if (id < 0) break outer;
                }
                System.out.println("i=" + i + ",j=" + j);
            }
        }
        for (int index : indexs) {
            System.out.print(a[index]);
        }

    }

}
