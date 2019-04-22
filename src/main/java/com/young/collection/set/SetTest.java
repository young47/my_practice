package com.young.collection.set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.HashSet;

public class SetTest {
    public static void main(String[] args){
        HashSet<String> set1 = Sets.newHashSet();
        HashSet<String> set2 = Sets.newHashSet();

        set1.add("John");
        set1.add("Tom");
        set1.add("tom");
        System.out.println(set1);
        set1.retainAll(set2);
        System.out.println(set1);

        String s="success", s1="Success", res = "successful";
        System.out.println(res.contains(s));
        System.out.println(res.contains(s1));

        HashMap<String, String> map = Maps.newHashMap();
        map.put("abc","abc");
        map.put("ABC","123");
        System.out.println(map);



    }
}
