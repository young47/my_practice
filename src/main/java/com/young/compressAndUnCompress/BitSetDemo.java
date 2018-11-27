package com.young.compressAndUnCompress;

import java.util.BitSet;

public class BitSetDemo {
    private BitSet bitSet;

    public BitSetDemo(long[] longs){
        this.bitSet = BitSet.valueOf(longs);
    }
}
