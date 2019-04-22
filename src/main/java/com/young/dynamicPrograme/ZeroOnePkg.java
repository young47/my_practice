package com.young.dynamicPrograme;

public class ZeroOnePkg {
    private static int capacity = 11;
    private static int[] elements = {3, 1, 5, 2, 4};

    private static int cw = 0; //当前权重之和
    private static int max = 0;
    private static boolean over = false;
    private static boolean[][] his = new boolean[elements.length][capacity + 1];

    public static void main(String[] args) {
        //backTrace();//回溯
        backTrace2();
        System.out.println("max = " + max);
        //btCostTest(1000_0000);
    }

    private static void btCostTest(int n) {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            backTrace2();
        }
        long end = System.currentTimeMillis();
        System.out.println("Iter " + n + " times cost : " + (end - begin) + "ms");
    }

    //回溯算法（遍历算法）
    //算法思想，所有的组合都试一遍
    private static void backTrace() {
        for (int i = 0; i < elements.length; i++) {
            if (over) {
                break;
            }
            cw = 0;
            forward(i, true);
        }
        System.out.println("max = " + max);
    }

    //每次都要从第i个元素开始组合，只向后，不向前
    //每个i遍历后，会得到一个最大值
    private static void forward(int i, boolean includeCurr) {
        if (includeCurr) {
            cw += elements[i];
        }
        if (cw == capacity || i == elements.length - 1) {//说明到最后一个元素或者装满
            if (cw == capacity) {
                over = true;
            }
            if (cw > max) {
                max = cw;
            }
            //System.out.print("(" + elements[i] + "," + includeCurr + ") cw=" + cw + ",max=" + max + " ");
            return;
        }
        //System.out.print("(" + elements[i] + "," + includeCurr + ") cw=" + cw + ",max=" + max + " ");
        //后面跟着的元素要么计算，要么跳过
        //跳过后面的元素
        forward(i + 1, false);
        //不跳过
        if ((cw + elements[i + 1]) <= capacity) {
            forward(i + 1, true);
        }
    }

    private static void backTrace2() {
        for (int i = 0; i < elements.length; i++) {
            forward2(i, elements[i]);
        }
    }

    //每次forward返回后，总能确定一个max值，forward(i, cw) <---> max一一对应。
    //在计算forward(0, 3)时会计算forward(1, 3)-不包含elements[1] 和 forward(1, 4)-包含elements[1]等，这些值在计算forward(2, true)时也会用到，将这些共有值保存起来就是优化点
    //将这些中间值保存在一个二维数组里
    private static void forward2(int i, int cw) {
        if (his[i][cw]) return;//已经计算过
        else his[i][cw] = true;
        if (cw == capacity || i == elements.length - 1) {
            if (cw > max) {
                max = cw;
            }
            return;
        }
        forward2(i + 1, cw);
        int cw2 = cw + elements[i + 1];
        if (cw2 <= capacity) {
            forward2(i + 1, cw2);
        }
    }

}
