package com.young.guava;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.math.LongMath;
import com.google.common.primitives.Longs;

import java.math.RoundingMode;

public class MyBloomFilter {

    private long[] data;
    private long bitSetCount;
    private long numBits;
    private long numHashFunctions;

    private MyBloomFilter(long numBits, long numHashFunctions) {
        this.data = new long[(int) LongMath.divide(numBits, 64, RoundingMode.CEILING)];
        this.numBits = numBits;
        this.numHashFunctions = numHashFunctions;
    }

    public static MyBloomFilter build(int expectedInsertions /* n */, double fpp) {
        long numBits = optimalNumOfBits(expectedInsertions, fpp);
        long numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
        return new MyBloomFilter(numBits, numHashFunctions);
    }

    public boolean put(String s) {
        long bitSize = data.length * Long.SIZE;
        byte[] bytes = Hashing.murmur3_128().hashString(s, Charsets.UTF_8).asBytes();
        long hash1 = lowerEight(bytes);
        long hash2 = upperEight(bytes);

        boolean bitsChanged = false;
        long combinedHash = hash1;
        for (int i = 0; i < numHashFunctions; i++) {
            // Make the combined hash positive and indexable
            bitsChanged |= set((combinedHash & Long.MAX_VALUE) % bitSize);
            combinedHash += hash2;
        }
        return bitsChanged;
    }

    public boolean mightContain(String target) {
        long bitSize = data.length * Long.SIZE;
        byte[] bytes = Hashing.murmur3_128().hashString(target, Charsets.UTF_8).asBytes();
        long hash1 = lowerEight(bytes);
        long hash2 = upperEight(bytes);

        long combinedHash = hash1;
        for (int i = 0; i < numHashFunctions; i++) {
            // Make the combined hash positive and indexable
            if (!get((combinedHash & Long.MAX_VALUE) % bitSize)) {
                return false;
            }
            combinedHash += hash2;
        }
        return true;
    }

    public long[] getData() {
        return data;
    }

    public byte[] getByte() {
        final byte[] bytes = new byte[data.length * 8];
        for (int i = 0; i < data.length; i++) {
            final long datum = data[i];
            int offset = i * 8;
            bytes[offset] = (byte) (datum & 0xFF);
            bytes[offset + 1] = (byte) (datum >> 8 & 0xFF);
            bytes[offset + 2] = (byte) (datum >> 16 & 0xFF);
            bytes[offset + 3] = (byte) (datum >> 24 & 0xFF);
            bytes[offset + 4] = (byte) (datum >> 32 & 0xFF);
            bytes[offset + 5] = (byte) (datum >> 40 & 0xFF);
            bytes[offset + 6] = (byte) (datum >> 48 & 0xFF);
            bytes[offset + 7] = (byte) (datum >> 56 & 0xFF);
        }
        return bytes;
    }
    private /* static */ long lowerEight(byte[] bytes) {
        return Longs.fromBytes(
                bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
    }

    private /* static */ long upperEight(byte[] bytes) {
        return Longs.fromBytes(
                bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
    }
    private boolean set(long index) {
        if (!get(index)) {
            data[(int) (index >>> 6)] |= (1L << index);
            bitSetCount++;
            return true;
        }
        return false;
    }

    private boolean get(long index) {
        return (data[(int) (index >>> 6)] & (1L << index)) != 0;
    }

    private static long optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    private static int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round(m / n * Math.log(2)));
    }

    public long getBitSetCount() {
        return bitSetCount;
    }

    public long getNumBits() {
        return numBits;
    }

    public long getNumHashFunctions() {
        return numHashFunctions;
    }
}
