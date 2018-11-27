package com.young.collection.map;

import java.util.BitSet;

public class BitSetTest {

    public static void main(String[] args){
        final BitSet bitSet = new BitSet(102400);
        final boolean b = bitSet.get(511);
        bitSet.set(10235);
        System.out.println("b="+b);
    }
}
