package com.young.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by young on 18/1/4.
 */
public class GenericArray {
    public static void main(String[] args) {
        array_1();
        //array_3();
        //array_2();
        List<String> stringLists = new ArrayList<String>(2);  // 假设可以创建泛型数组

    }

    private static void array_3() {
        Object[] oa = new List[10];
        List<Integer> li = new ArrayList<Integer>();
        li.add(new Integer(3));
        oa[1] = li; // Unsound, but passes run time store check
        //String s = ((List)oa[1]).get(0); // Run-time error: ClassCastException.
        //System.out.println(s);
    }

    private static void array_2() {
        List<?>[] lsa = new List<?>[10]; // OK, array of unbounded wildcard type.
        Object[] oa = lsa;
        List<Integer> li = new ArrayList<Integer>();
        li.add(new Integer(3));
        oa[1] = li; // Correct.
        Integer i = (Integer) lsa[1].get(0); // OK
        System.out.println(i);

        List<String> list = new ArrayList<>();
        list.add("hahaha");
        oa[2] = list;
        String o1 = (String) lsa[2].get(0);
        System.out.println(o1);

    }

    private static void array_1() {
        //List<String>[] lsa = new List<String>[10]; // Not really allowed.
        List<String>[] lsa = new List[10]; // Not really allowed.
        Object[] oa = lsa;
        List<Integer> li = new ArrayList<Integer>();
        li.add(new Integer(3));
        oa[1] = li; // Unsound, but passes run time store check
        //lsa[1] = li;
        String s = lsa[1].get(0); // Run-time error: ClassCastException.
        System.out.println(s);

    }
}
