package com.young.sort;


public class HeapSort {
    public static void sort(int[] arrays) {
       /* int length = arrays.length;
        int[] newArrays = new int[length + 1];
        newArrays[0] = 0;
        System.arraycopy(arrays, 0, newArrays, 1, length);*/
        buildHeap(arrays);
        //
        //buildHeap2(arrays);

        //System.arraycopy(newArrays, 1, arrays, 0, length);
    }

    /**
     * 思路：假设堆中只有一个元素(第一个)，把2~n个元素插入进去
     *
     * @param arrays
     */
    private static void buildHeap(int[] arrays) {
        for (int i = 1; i < arrays.length; i++) {
            int j = i;
            while (j >= 1 && arrays[j] < arrays[(j - 1) / 2]) {//小于父节点
                swap(arrays, j, (j - 1) / 2);
                j = (j - 1) / 2;
            }
        }
    }

    /**
     * 思路：从第一个非叶子节点开始，依次与其叶子节点比较
     *
     * @param arrays
     */
    private static void buildHeap2(int[] arrays) {
        int length = arrays.length;
        for (int i = (length - 1) / 2; i >= 0; i--) {
            int minPos = i;
            int j = i;
            while (true) {
                if ((j * 2 + 1) <= length - 1 && arrays[j] > arrays[j * 2 + 1]){
                    minPos = j * 2 + 1; //左子树较小
                }
                if ((j * 2 + 2) <= length - 1 && arrays[minPos] > arrays[j * 2 + 2]){
                    minPos = j * 2 + 2;//右子树最小
                }
                if (minPos == j){//根节点最小
                    break;
                }
                swap(arrays, j, minPos);
                j = minPos;//交换后，左右子树的大小可能发生了变化，继续向下交换
            }

        }
    }

    private static void swap(int[] arrays, int i, int j) {
        int temp = arrays[i];
        arrays[i] = arrays[j];
        arrays[j] = temp;
    }

    private static void printHeap(int[] heap) {
        int deepth = heap.length / 2;
        int k = 0;
        for (int i = 1; i <= deepth && k < heap.length; i++) {
            for (int j = 0; j < (1 << (i - 1)) && k < heap.length; j++) {
                System.out.print(heap[k++] + " ");
            }
            System.out.println(" ");
        }
    }

    public static void main(String[] args) {
        int[] arrays = {7, 4, 6, 2, 1, 10, 24, -1};
        //buildHeap(arrays);
        buildHeap2(arrays);
        printHeap(arrays);
    }
}
