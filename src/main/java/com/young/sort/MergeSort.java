package com.young.sort;

public class MergeSort {
    public static void mergeSort(int[] arrays) {
        msort(arrays, 0, arrays.length - 1);
    }

    private static void msort(int[] arrays, int start, int end) {
        if (end <= start) {
            return;
        }
        int middle = (start + end) / 2;
        while (end > start) {
            msort(arrays, start, middle);
            msort(arrays, middle + 1, end);
            merge(arrays, start, middle, end);
        }
    }

    private static void merge(int[] arrays, int start, int middle, int end) {
        int[] merged = new int[end - start + 1];
        int i = start, j = middle + 1, k = 0;
        while (i <= middle && j <= end) {
            if (arrays[i] < arrays[j]) {
                merged[k++] = arrays[i++];
            } else {
                merged[k++] = arrays[j++];
            }
        }
        //上面的循环移动了相同数量的元素，可能还有剩余
        //左边数组有剩余
        while (i <= middle) {
            merged[k++] = arrays[i++];
        }
        //右边有剩余
        while (j <= end) {
            merged[k++] = arrays[j++];
        }

        //将临时数组中数据放入数组中
        for (int l = 0; l < merged.length; l++) {
            arrays[start++] = merged[l];
        }
    }
}
