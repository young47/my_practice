package com.young.reference;

import java.util.WeakHashMap;

/**
 * Created by young on 17/11/3.
 */
public class WeakHashMapTest {

    public static void main(String[] args) {
        int i = 0;
        WeakHashMap whm = new WeakHashMap();
        while (i++<10000){
            whm.put(new Integer(i), "第"+i+"个value");
            System.out.println(whm.size());
        }
    }
}
