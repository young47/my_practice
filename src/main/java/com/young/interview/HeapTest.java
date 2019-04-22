package com.young.interview;

import com.young.sort.Sort;

/**
 * 堆化(就是向堆中插入，删除一个元素)的时间复杂度是O(logn)
 * 建堆的时间复杂度是O(n)，对方法2而言
 * 堆排序的时间复杂度是O(nlogn)
 */
public class HeapTest {

    private static int swap1 = 0, swap2 = 0;
    public static void main(String[] args) {
        int[] arrays = {10, 3, 4, 9, 3, 8, 1, 26, 7, 2};
        buildHeap(arrays);
        //buildCostTest();
        insert(-1);
    }

    //方法2比方法1效率高，方法2的时间复杂度为O(n), 方法1的时间复杂度为O(nlogn)
    //法2是从中间节点开始向前，只遍历了一半节点，这也是比较快的一个原因吧。
    private static void buildCostTest() {
        int count = 10000, size = 1_00000;
        long begin = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            method1(Sort.generateArrays(size));
        }
        long end = System.currentTimeMillis();
        System.out.println("method1 cost : " + (end - begin) + "ms");
        System.out.println("swap="+swap1);

        for (int i = 0; i < count; i++) {
            method2(Sort.generateArrays(size));
        }
        long end2 = System.currentTimeMillis();
        System.out.println("method2 cost : " + (end2 - end) + "ms");
        System.out.println("swap="+swap2);
    }

    //时间复杂度分析
    //方法1是从下向上比较，因此每个节点找到合适位置与其深度成正比，depth=logn
    private static void buildHeap(int[] arrays) {
        method1(arrays);
        print(arrays);
        int[] arrays1 = {10, 3, 4, 9, 3, 8, 1, 26, 7, 2};
        method2(arrays1);
        print(arrays1);
    }

    //从上向下寻找，直到适当的位置，因此每个节点找到合适位置与其高度成正比，height=logn
    private static void method2(int[] arrays) {
        int n = arrays.length;
        for (int i = n / 2 - 1; i >= 0; i--) {//从第一个非叶子节点开始
            //每个节点都向下去和叶子比较直到最底
            int j = i;
            while ((2 * j + 1) <= n - 1) {//有左子节点
                int left = 2 * j + 1, right = 2 * j + 2, min = left;
                if (right <= n - 1) {
                    //判断左右节点哪个更小
                    min = arrays[left] > arrays[right] ? right : left;
                } else { //没有右子节点
                    min = left;
                }
                if (arrays[j] > arrays[min]) {
                    swap(arrays, j, min); //和较小的叶子节点交换
                    swap2++;
                }
                j = min; //下移到叶子层继续比较
            }
        }
    }

    //从下向上寻找，直到适当的位置
    private static void method1(int[] arrays) {
        for (int i = 1; i < arrays.length; i++) {
            int j = i;
            //每个节点都向上去和父节点比较直到root
            while (j != 0) { //没有到root
                int pi = (j - 1) >> 1; // parent index
                if (arrays[pi] > arrays[j]) {
                    swap(arrays, j, pi);
                    j = pi;
                    swap1++;
                } else {
                    break;
                }
            }
        }
        //print(arrays);
    }
    //插入操作：把新的数据放在最后，然后如同method1()向上比较找到合适的位置，时间复杂度与深度成正比，是O(logn)
    private static void insert(int i) {

    }

    //删除堆顶元素：交换堆顶和最后一个元素，然后向下比较找到合适的位置，时间复杂度与高度成正比，是O(logn)
    private static void removeRoot(){

    }

    public static void print(int[] arrays) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arrays.length; i++) {
            if (i == arrays.length - 1) {
                sb.append(arrays[i]);
            } else {
                sb.append(arrays[i]).append(",");
            }
        }
        sb.append("]");
        System.out.println(sb);
    }


    private static void swap(int[] arrays, int pi, int i) {
        int tmp = arrays[pi];
        arrays[pi] = arrays[i];
        arrays[i] = tmp;
    }
}
