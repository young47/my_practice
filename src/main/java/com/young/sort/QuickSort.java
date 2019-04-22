package com.young.sort;

public class QuickSort {

    public static void sort(int[] arrays) {
        sortQuick(arrays, 0, arrays.length - 1);
    }

    /**
     * 快速排序的优化
     * 1. 简单快排(二路快排)
     * 2. 随机快排
     * pivot 不再是选择开头或结尾的元素，而是随机选取一个元素，或抽查若干个元素，取中值；为了便于统一编码，若选择的pivot不是end，则交换pivot与end，这样就可以利用简单快排的代码了
     * 这种随机选取pivot的方式对一个近乎有序的数组而言比较好，否则时间复杂度极可能退化成O(n^2)
     * 3. 三路快排
     * 如果数组中含有大量重复元素，上面两种快排效率都比较低；此时可以将数组分成 <、=和>三个区间
     *
     * @param arrays
     * @param begin
     * @param end
     */
    private static void sortQuick(int[] arrays, int begin, int end) {
        if (begin >= end) {
            return;
        }
        //简单快排
        //int pivot = partition(arrays, begin, end);
        //随机快排
        int pivot = randomPartition(arrays, begin, end);

        sortQuick(arrays, begin, pivot - 1);
        sortQuick(arrays, pivot + 1, end);

        //三路快排
        //threePartition(arrays, begin, end);
    }

    private static void threePartition(int[] arrays, int begin, int end) {
        if (begin >= end){
            return;
        }
        int pivotValue = arrays[end];
        int p = begin, q = end, i = begin;
        while (p <= q) {
            if (arrays[p] < pivotValue) {
                swap(arrays, p++, i++);
            } else if (arrays[p] > pivotValue) {
                swap(arrays, p, q--);
            } else {
                p++;
            }
        }
        //Sort.print(arrays);
        //i指向第一个等于pivot的数，q指向最后一个
        threePartition(arrays, begin, i-1);
        threePartition(arrays, q+1, end);
    }

    private static int randomPartition(int[] arrays, int begin, int end) {
        //最后要加上 begin，因为pivot是下标
        int pivot = (int) (Math.random() * (end - begin + 1)) + begin;
        if (pivot != end) {
            //若不是最后一个元素，交换
            swap(arrays, pivot, end);
        }
        return partition(arrays, begin, end);

    }

    private static int partition(int[] arrays, int begin, int end) {
        int pivot = end;
        int value = arrays[pivot];
        //high不要设为 end - 1，否则当只有两个元素时，就是错误的；
        //刚开始就犯了这个错误
        int low = begin, high = end;
        //挪动元素，pivot左边小，pivot右边大
        //如果选择的pivot在最左边，则要先从右边开始向左找；若pivot在最右边，则要先从左边开始找；
        // 比如pivot设在最右边，先从左边开始找可以保证交换后小的都在pivot左边；
        while (low < high) {
            //找第一个大于pivot的值
            while (low < high && arrays[low] <= value) {
                low++;
            }
            //找最后一个小于pivot的值
            while (low < high && arrays[high] >= value) {
                high--;
            }
            //此时low应和high的交换
            if (low < high) {
                swap(arrays, low, high);
            }
        }
        /*if (arrays[low] > value) {
            swap(arrays, low, pivot);
        }*/
        //此时low必定=high
        swap(arrays, low, pivot);
        //Sort.print(arrays);
        //System.out.println("交换了第 " + low + "，" + pivot + " 个元素");
        return low;
    }


    private static void swap(int[] arrays, int low, int high) {
        int temp = arrays[low];
        arrays[low] = arrays[high];
        arrays[high] = temp;
    }

    public static void main(String[] args) {
        int[] arrays = {3, -1, 20, 49, 10};
        sort(arrays);
        System.out.println("sorted:");
        Sort.print(arrays);

    }
}
