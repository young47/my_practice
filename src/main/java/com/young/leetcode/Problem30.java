package com.young.leetcode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by young on 17/12/26.
 */
public class Problem30 {
    public static void main(String[] args) {
        String[] words = {"bar","foo","the"};
        List<Integer> result = findSubstring("barfoofoobarthefoobarman", words);
        System.out.println(result);
    }

    public static List<Integer> findSubstring(String s, String[] words) {
        int c = words.length;
        String ss = words[0];
        int len1 = ss.length();
        int len = s.length();

        List<Integer> indexList = new ArrayList<>();
        TreeMap<Integer, String> map = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer pre, Integer cur) {
                return pre.compareTo(cur);
            }
        });
        for (int i = 0; i < c; i++) {
            String item = words[i];
            if (!s.contains(item)) {
                return null;
            }
            int k = -len1;
            do {
                k = s.indexOf(item, k + len1);
                if (k >= 0 && !map.containsKey(k))
                    map.put(k, item);
                else {
                    break;
                }
            } while ((k + len1) < len);

        }
        List<Integer> indexs = new ArrayList<>(map.keySet());

        for (int k = 0; k <= indexs.size() - c; k++) {
            String[] temp = words;
            for (int i = 0; i < c - 1; i++) {
                int curIndex = indexs.get(i + k);
                int nextIndex = indexs.get(i + k + 1);
                if (nextIndex - curIndex == len1) {
                    String s1 = map.get(curIndex);
                    String s2 = map.get(nextIndex);
                    boolean exists = false;
                    for (String s3 : temp) {
                        if (s3.equals(s1)||s3.equals(s2)){
                            s3 = "";
                            exists = true;
                            break;
                        }
                    }
                    if (!exists){
                        break;
                    }
                    if (i == c - 2) {
                        indexList.add(indexs.get(k));
                    }
                } else {
                    break;
                }
            }
        }

        return indexList;

    }
}
