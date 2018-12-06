package com.young.sort;

import io.netty.bootstrap.Bootstrap;
import org.apache.hadoop.util.MergeSort;

import java.util.Arrays;
import java.util.Random;

/**
 * 下述算法 从 小 -> 大 排序
 * <1>
 * 冒泡和插入是稳定排序，选择排序不是；
 * 我觉得最根本的原因是：前二者实际上都经过了相邻元素比较的步骤(插入是相邻元素的挪动，实际上相当于比较了)，当相邻的元素相等时，不让交换就可以了；而选择排序并没有这个步骤，而是会跳跃
 * <p>
 * 比如： 5A, 4, 5B, 2
 * 冒泡：当5A和5B比较时，我们可以不交换，结果： 2，4，5A，5B
 * 插入：经过一次插入过程，数组变为 4，5A，5B，2；当插入5B时，我们可以控制让5B保持原位
 * 选择： 2，4，5B，5A，两个5交换了顺序
 * <p>
 * <2>
 * 冒泡可以优化：当内循环无数据交换时，可以说明已经排序完成；因为内循环时，相邻元素都要见一次面，所以只有是一个有序数组时才可能不发生数据交换
 * 插入没有优化：每次插入一个元素时，只是定位到了这个元素的相对位置，如上面例子第一次遍历后 4，5A，5B，2；4放到了5A前，但这只是完成了4和5A相对位置的确定，最终的位置必须将所有的元素遍历完成；
 */
public class Sort {
    //数组中包含的元素个数
    private static final int SIZE = 1000;

    //数组个数
    private static final int CNT = 1000;

    public static void main(String[] args) {
        timeCost();
        int[] arrays = {3, -1, 20, 49, 10, 1, 0, 35, 20};
        //bubbleSort(arrays);

        //selectSort(arrays);

        //insertSort(arrays);

        //bubbleSort2(arrays);

        //shellSort(arrays);

        //halfInsertSort(arrays);
        //print(arrays);

    }

    private static void timeCost() {
        final long begin = System.currentTimeMillis();
        for (int i = 0; i < CNT; i++) {
            //System.out.println("第"+(i+1)+"个数组：");
            int[] arrays = generateArrays(SIZE);
            //bubbleSort(arrays);
            //bubbleSort2(arrays);

            //selectSort(arrays);

            //insertSort(arrays);
            halfInsertSort(arrays);
            //shellSort(arrays);

            mergeSort(arrays);

            //print(arrays);

        }
        final long end = System.currentTimeMillis();
        System.out.println(CNT + "个数组[" + SIZE + "个元素]排序消耗：" + (end - begin) + "ms");
    }

    /**
     * 内循环只能确定一个元素的位置，所以n个元素，外循环就得是n次
     */

    /**
     * 这种算法实际上操作的是无序数组，总是在无序数组中两两比较交换
     *
     * @param arrays
     */
    private static void bubbleSort(int[] arrays) {
        final int n = arrays.length;
        for (int i = 0; i <= n - 1; i++) { //总计要排序n次，每次可以确定一个元素的位置
            for (int j = 0; j < n - 1 - i; j++) {
                if (arrays[j] > arrays[j + 1]) {
                    swap(arrays, j, j + 1);
                }
            }
        }
    }

    /**
     * 冒泡的优化
     * <p>
     * 当数组已经是有序数组时，内循环走一次就结束，即exchange=false。时间复杂度变为O(n)
     *
     * @param arrays
     */
    private static void bubbleSort2(int[] arrays) {
        final int n = arrays.length;
        for (int i = 0; i <= n - 1; i++) { //总计要排序n次，每次可以确定一个元素的位置
            boolean exchange = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (arrays[j] > arrays[j + 1]) {
                    swap(arrays, j, j + 1);
                    exchange = true; //说明有数据交换
                }
            }
            if (!exchange) { //没有数据交换，实际上已排好序
                return;
            }
        }
    }

    /**
     * 插入排序思想： 分成有序和无序数组两部分，每次从无序数组中拿一个(通常是第一个)插入到有序数组的适当位置，插入之前要挪动若干个有序元素
     * <p>
     * 这种算法实际上操作的是有序数组，总是移动这个数组中某些数
     * <p>
     * 当数组已经是有序数组时，就不再进入内循环。时间复杂度变为O(n)
     * <p>
     * 减少内循环的次数，可以提高效率。正如冒泡排序的优化，实际就是减少了内循环的次数；对于插入排序(冒泡排序也如此)，当序列为有序时，内循环不会运行多次；比如：
     * 1，2，5，3，-1，前面1，2，5实际上并没有走内循环(一次就跳出)，到3的时候，会遍历前面的有序数组；
     * 因此，优化的一个方向就是如何让数组的一部分尽量变得有序。这里希尔排序就闪亮登场了
     *
     * @param arrays
     */
    private static void insertSort(int[] arrays) {
        final int n = arrays.length;
        for (int i = 0; i < n; i++) {
            int value = arrays[i]; //arrays[0... i-1]为有序数组，arrays[i... n-1]为无序数组
            int j = i - 1;
            //还有一种优化方法就是这个内循环改为二分查找，请看后面的halfInsertSort()
            for (; j >= 0; j--) { //将arrays[i]插入有序数组中，倒序遍历有序数组找插入位置
                if (value < arrays[j]) {
                    arrays[j + 1] = arrays[j];
                } else {
                    break;
                }
            }
            arrays[j + 1] = value;
        }
    }

    /**
     * 希尔排序
     * <p>
     * 非稳定排序
     *
     * @param arrays
     */
    private static void shellSort(int[] arrays) {
        final int n = arrays.length;
        int inc = n, value;
        while (inc > 1) {
            inc = inc / 3 + 1;
            for (int i = 0; i <= inc - 1; i++) { //对分隔的inc个子数组进行插入排序
                //对 arrays[i+0*inc],arrays[i+1*inc],arrays[i+2*inc],arrays[i+k*inc]进行插入排序
                //不再是相邻的元素比较移动，而是相隔inc的元素比较移动
                for (int P = i; P <= n - 1; P += inc) { //k+1个元素循环k+1次;由于k+1的具体数字与有关，因此边界条件直接使用P<=n-1
                    value = arrays[P];
                    int Q = P - inc;
                    for (; Q >= 0; Q -= inc) {
                        if (arrays[Q] > value) {
                            arrays[Q + inc] = arrays[Q];
                        } else {
                            break;
                        }
                    }
                    arrays[Q + inc] = value;
                }
            }
        }
    }

    /**
     * @param arrays
     */
    private static void halfInsertSort(int[] arrays) {
        int n = arrays.length, value;
        int pos;
        for (int i = 1; i <= n - 1; i++) {
            value = arrays[i];
            //通过二分查找定位value应该插入的位置
            if (value >= arrays[i - 1]) {
                continue;
            } else {
                pos = binarySearch(arrays, value, 0, i - 1);
            }
            for (int j = i - 1; j >= pos; j--) {
                arrays[j + 1] = arrays[j];
            }
            arrays[pos] = value;
        }
    }


    /**
     * 选择排序思想： 分成有序和无序数组两部分，每次从无序数组中找到最小的元素
     * <p>
     * 这种算法实际上操作的是无序数组，总是在无序数组中查找最小元素并放在无序数组的第一个(第一个前面就是有序数组的最后一个)
     * <p>
     * 当数组已经是有序数组时，就不再进入内循环。时间复杂度变为O(n)
     *
     * @param arrays
     */
    private static void selectSort(int[] arrays) {
        final int length = arrays.length;
        for (int i = 0; i <= length - 1; i++) { //arrays[0... i-1]为有序数组，arrays[i... n-1]为无序数组
            int min = i;//每次总是假设无序数组的第一个为最小元素
            for (int j = i + 1; j <= length - 1; j++) { //在无序数组剩余元素中找还有没有更小的元素
                if (arrays[j] < arrays[min]) {
                    min = j; //寻找最小元素的index
                }
            }
            swap(arrays, i, min);//无序数组的第一个元素设为了当前的最小值
        }
    }

    /**
     * @param arrays
     */
    private static void selectSort1(int[] arrays) {
        final int length = arrays.length;
        for (int i = 0; i < length - 1; i++) {
            for (int j = i + 1; j <= length - 1; j++) {
                if (arrays[i] < arrays[j]) {
                    swap(arrays, i, j);//这里实际上不用老交换，这一步的目的实际上就是找到最小的值给arrays[i]；应该像上面的找到最小值，最后赋值一次就可以
                }
            }
        }
    }

    private static void swap(int[] arrays, int i, int j) {
        if (i != j) {
            int temp = arrays[i];
            arrays[i] = arrays[j];
            arrays[j] = temp;
        }

    }

    /**
     * 为了算法稳定，我们要试图在最右面的位置插入元素
     * <p>
     * 比如： 1，2a，2b | 2c， 现在要插入2c，算法保持稳定的位置是在2b后，因此利用二分在查找元素时，对于相等的元素，我们要再去它的右边找适当的位置
     * <p>
     * 若要去找第一个相等的元素，则要把mid相等的元素划到左区间中
     *
     * @param arrays
     * @param target
     * @param start
     * @param end
     * @return
     */
    private static int binarySearch(int[] arrays, int target, int start, int end) {
        while (start <= end) {//start=end表明只剩下一个元素，也要判断一下这个元素是否为要找的元素
            int mid = (start + end) / 2;
            if (arrays[mid] > target) {//target在左区间中[,)半闭半开；"="若放在这里，那就是去左区间中寻找，算法不再稳定
                end = mid - 1;
            } else { //target在右区间中 [,]，右区间可能不止一个target，这里需要找到最右边的target；此时mid处的值可能已经相等，我们有必要从mid+1处判断一下(若mid不相等，那就更得找了)
                start = mid + 1;
            }
        }
        return start;
    }

    //merge(sort(p,q), sort(q+1, r))
    private static void mergeSort(int[] arrays) {
        msort(arrays, 0, arrays.length);
    }
    private static void msort(int[] arrays, int start, int end){
        if (end <= start) {
            return;
        }
        int middle = (start + end) / 2;
        while (end > start) {
            msort(arrays, start, middle);
            msort(arrays, middle + 1, end);
            int[] tmp = new int[end - start + 1];
            //merge(tmp, );
        }

    }
    private static void merge(int[] arrays1, int[] arrays2){

    }

    private static void print(int[] arrays) {
        for (int array : arrays) {
            System.out.print(array + ",");
        }
        System.out.println("");
    }


    /**
     * @param size size of array elements
     * @return
     */
    private static int[] generateArrays(int size) {
        final Random random = new Random();
        final int[] ints = new int[size];
        for (int j = 0; j < size; j++) {
            ints[j] = random.nextInt(size) + 1;
        }
        return ints;
    }
}
