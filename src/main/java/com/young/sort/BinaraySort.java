package com.young.sort;

/**
 * 二分查找注意点：
 * 1. 循环退出 low<=high, 不是low<high;
 * 2. mid=(low+high)/2 可能会溢出；使用 low+(high-low)/2或 low+(high-low)>>1更好
 * 3. low=mid+1 或 high=mid-1; 如果low=mid或high=mid可能造成死循环
 */
public class BinaraySort {

    public static void main(String[] args) {
        //int[] arrays = {1, 2, 5, 5, 7, 9, 9, 9, 9, 20};
        int[] arrays = {1, 3, 4, 6};
        int value = 5, start = 0, end = arrays.length - 1;

        //查找第一个值等于给定值的元素
        System.out.println("第一个等于" + value + "的元素index=" + firstEqual(arrays, value, start, end));

        //查找最后一个值等于给定值的元素
        System.out.println("最后一个等于" + value + "的元素index=" + lastEqual(arrays, value, start, end));

        //查找第一个大于等于给定值的元素
        System.out.println("第一个大于等于" + value + "的元素index=" + firstGreaterOrEqualTo(arrays, value, start, end));

        //查找最后一个小于等于给定值的元素
        //value = 6;
        System.out.println("最后一个小于等于" + value + "的元素index=" + lastLessOrEqualTo(arrays, value, start, end));

        //是否包含某个值
        //value = 19;
        System.out.println("是否包含value=" + value + "的元素 " + contains(arrays, value, start, end));

        //包含多少个value
        System.out.println("包含value=" + value + "的元素个数" + howMany(arrays, value, start, end));

        //求一个数的平方根，精确到小数点后6位
        float target = 7f;
        int precision = 6;
        //System.out.println("value=" + value + "的平方根是 " + squrt(target, precision));


    }

    private static int howMany(int[] arrays, int value, int start, int end) {
        final int firstEqual = firstEqual(arrays, value, start, end);
        final int lastEqual = lastEqual(arrays, value, start, end);
        return lastEqual - firstEqual + 1;
    }

    private static boolean contains(int[] arrays, int value, int start, int end) {
        while (start <= end) {
            int mid = (start + end) / 2;
            if (arrays[mid] > value) {
                end = mid - 1;
            } else if (arrays[mid] < value) {
                start = mid + 1;
            } else {
                return true;
            }
        }
        return false;
    }

    private static int lastLessOrEqualTo(int[] arrays, int value, int start, int end) {
        while (start <= end) {
            int mid = (start + end) / 2;
            if (arrays[mid] > value) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return start - 1;
    }

    private static int firstGreaterOrEqualTo(int[] arrays, int value, int start, int end) {
        while (start <= end) {
            int mid = (start + end) / 2;
            if (arrays[mid] >= value) {//等于的时候，要向前找，因为前面的数也可能相等
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return end + 1;
    }

    private static float lastLessOrEqualTo(float[] arrays, float value, int start, int end) {
        while (start <= end){
            int mid = start + (end-start)/2;
            if (arrays[mid]>value){
                end = mid -1;
            }else { //等于的时候，要向后找，因为后面的数也可能相等
                start = mid + 1;
            }
        }
        return arrays[start - 1];
    }

    private static int lastEqual(int[] arrays, int value, int start, int end) {
        while (start <= end) {
            int mid = (start + end) / 2;
            if (arrays[mid] > value) {
                end = mid - 1;
            } else if (arrays[mid] < value) {
                start = mid + 1;
            } else {
                if (arrays[mid + 1] != value) {
                    return mid;
                }
                return lastEqual(arrays, value, mid, end);
            }
        }
        return -1;
    }

    private static int firstEqual(int[] arrays, int value, int start, int end) {
        while (start <= end) {
            int mid = (start + end) / 2;
            if (arrays[mid] > value) {
                end = mid - 1;
            } else if (arrays[mid] < value) {
                start = mid + 1;
            } else {
                if (mid - 1 >= 0 && arrays[mid - 1] != value) {
                    return mid;
                }
                return firstEqual(arrays, value, start, mid);
            }
        }
        return -1;
    }

    private static float squrt(float value, int precision) {
        int i = 0;
        float result = 0f, min = 1f;
        while ((i++) <= 6) {
            int j = 0;
            float base = 1.0f, span = 1.0f;
            while ((++j) <= i) {
                base *= 10;
            }
            span = 1 / base;
            float[] arrays = constructArrays(min, value, span);
            final float lastLessOrEqualTo = lastLessOrEqualTo(arrays, value, 0, arrays.length - 1);
            result += lastLessOrEqualTo + base;
        }

        return result;
    }

    private static float[] constructArrays(float min, float max, float span) {
        float[] arrays = new float[(int) ((max - min + 1) / span)];
        for (int i = 0; i < arrays.length; i++) {
            arrays[i] = min + i * span;
        }
        return arrays;
    }
}
