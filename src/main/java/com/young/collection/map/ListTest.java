package com.young.collection.map;

import java.util.ArrayList;

public class ListTest {

    public static void main(String[] args) {
        final ArrayList<String> list = new ArrayList<>(4);
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        for (String s : list) {
            if ("d".equals(s)){
                list.remove(s);
            }
        }
    }
}
