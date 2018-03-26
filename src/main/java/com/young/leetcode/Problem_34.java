package com.young.leetcode;

import java.util.Arrays;

/**
 * Created by young on 18/1/4.
 */
public class Problem_34 {
    public static void main(String[] args) {
        int[] nums = {5,7,7,7,7,8,8,8,8,8,10};
        int target = 8;
        System.out.println("nums.length : "+nums.length);
        System.out.println(Arrays.toString(searchRange(nums, target)));
    }

    public static int[] searchRange(int[] nums, int target) {
        int start = 0, count = nums.length, end = count - 1;
        while (nums[start] < nums[end]) {
            int mid = (start + end) >> 1;
            if (nums[mid] < target) {
                start = mid + 1;
            } else if (nums[mid] > target) {
                end = mid - 1;
            } else {
                /*int i = mid - 1, j = mid + 1;
                while (i >= start && nums[i] == target) {
                    i--;
                }
                while (j <= end && nums[j] == target) {
                    j++;
                }
                return new int[]{i + 1, j - 1};*/

                //int left = findIndex(nums, target, start, mid, true);
                //int right = findIndex(nums, target, mid, end, false);
                //return new int[]{left, right};
                if (nums[start]==target){
                    end--;
                }else {
                    start++;
                }

            }
        }
        if (nums[start]==nums[end]&&nums[start]==target){
            return new int[]{start, end};
        }
        return new int[]{-1, -1};
    }

    private static int findIndex(int[] nums, int target, int start, int end, boolean left) {
        while (start <= end) {
            int mid = left ? (start + end) >> 1 : (start + end + 1) >> 1;
            if (nums[mid] < target) {
                start = mid + 1;
            } else if (nums[mid] > target) {
                end = mid - 1;
            } else if (nums[mid] == target) {
                if (left || mid == end){
                    return mid;
                }
            }
        }
        return -1;
    }
}

