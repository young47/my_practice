package com.young.leetcode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * Created by young on 17/9/5.
 */
public class Practice {
    public static void main(String[] args){
        StringBuilder[] sb = new StringBuilder[5];
        for (StringBuilder ssb : sb) {//必须先初始化sb中stringbuilder才能使用
            ssb.append("dd");
        }

        System.out.println(sb.toString());

        TreeMap<Integer, String> map = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        ArrayList<Integer> integers = new ArrayList<>(map.keySet());
    }

    public int[] twoSum(int[] nums, int target) {
        int c[] = new int[2];
        if(nums.length == 0){
            return c;
        }
        for(int i=0; i<nums.length-1; i++){
            for(int j=1; j<nums.length; j++){
                if(nums[i]+nums[j] == target){
                    c[0] = i;
                    c[1] = j;
                    return c;
                }
            }
        }
        return c;
    }
}
