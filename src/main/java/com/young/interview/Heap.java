package com.young.interview;

/**
 * 第i元素的父节点为 i/2-1, 左节点为 2*i+1，右节点为2*i+2
 */
public class Heap {

    private static int[] arrays = {10, 3, 4, 9, 3, 8, 1, 26, 7, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static int capacity = 19;//最大容量
    private static int limit = 10;//当前容量

    //将一个无序的数组初始化为小顶堆
    private static void buildHeap(int[] arrays, int n) {
        for (int i = n / 2 - 1; i >= 0; i--) {
            int j = i;
            while (left(j) <= n - 1) {
                int min = left(j);
                if (right(j) <= n - 1) {
                    min = valueOf(left(j)) > valueOf(right(j)) ? right(j) : left(j);
                }
                if (valueOf(j) > valueOf(min)) {
                    swap(arrays, j, min);
                }
                j = min;
            }
        }
    }

    private static void insert(int d) {
        arrays[limit] = d; //新的元素放在最后
        int j = limit;
        while (parent(j) >= 0) {
            if (valueOf(j) < valueOf(parent(j))) {
                swap(arrays, j, parent(j));
                j = parent(j);
            } else {
                break;
            }
        }
        limit++;
    }

    //删除堆顶
    private static void removeRoot() {
        swap(arrays, 0, limit - 1);//交换堆顶与最后一个元素
        int j = 0;
        limit--;
        while (left(j) <= limit - 1) {
            int min = left(j);
            if (right(j) <= limit - 1) {
                min = valueOf(left(j)) > valueOf(right(j)) ? right(j) : left(j);
            }
            if (valueOf(j) > valueOf(min)) {
                swap(arrays, j, min);
            }
            j = min;
        }
    }

    //调整p位置的元素，使堆重新成为小顶堆
    private static void adjustHeap(int[] arrays, int p) {
        while (parent(p) > 0) {
            if (valueOf(p) < valueOf(parent(p))) {
                swap(arrays, p, parent(p));
                p = parent(p);
            }
        }
    }

    private static int left(int i) {
        return 2 * i + 1;
    }

    private static int right(int i) {
        return 2 * i + 2;
    }

    private static int parent(int i) {
        return (i - 1) / 2;
    }

    private static int valueOf(int i) {
        return arrays[i];
    }

    private static void swap(int[] arrays, int pi, int i) {
        int tmp = arrays[pi];
        arrays[pi] = arrays[i];
        arrays[i] = tmp;
    }

    private static void printHeap(int[] arrays, int n) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < n; i++) {
            if (i == n - 1) {
                sb.append(arrays[i]);
            } else {
                sb.append(arrays[i]).append(",");
            }
        }
        sb.append("]");
        System.out.println(sb);
    }

    public static void main(String[] args) {
        buildHeap(arrays, limit);
        printHeap(arrays, limit);
        //insert(-1);
        removeRoot();
        printHeap(arrays, limit);
    }
}
