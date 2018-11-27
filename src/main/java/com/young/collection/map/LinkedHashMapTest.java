package com.young.collection.map;

import java.util.*;

/**
 * Created by young on 18/1/26.
 */
public class LinkedHashMapTest {
    Map<String, String> map = new LinkedHashMap();
    public static void main(String[] args){
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        for (String s : list) {
            if (s.equals("c")){
                list.remove(s);
            }
        }
        System.out.println(list.size());

        HashMap<String, Long> map = new HashMap<>();
//        map.put("a", 2l);
        Long a = map.computeIfPresent("a", (key, val) -> ++val);
        System.out.println("a="+a);
        Long b = map.computeIfPresent("b", (key, val)-> val++);
        System.out.println("b="+b);
        Long c = map.computeIfAbsent("c", val -> 90l);
        System.out.println("c="+c);

        map.merge("e", 24l, (oldValue, newValue)->++newValue);
        System.out.println(map);

        StringBuilder sb = new StringBuilder();
        sb.append("abc");
        sb.append("bff");
        sb.append("cgr");
        for (String s : sb.toString().split(",")) {
            System.out.println(s);
        }

    }

}
