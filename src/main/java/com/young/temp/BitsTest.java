package com.young.temp;

import org.junit.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static com.young.temp.BloomFilterParams.*;

public class BitsTest {
    private File DUMP_FILE;
    private static final String MODE = "rw";
    private static ByteBuffer mbb = ByteBuffer.allocate(32);
    //private static LongBuffer bloomBuffer;

    private static void initBuffer(long createTime, int keyCapacity, int keyInserted) {
        mbb.put(MAGIC).putLong(CTI, createTime).putInt(KCI, keyCapacity).putInt(KII, keyInserted);
        for (int i = BFI; i < mbb.limit(); i++) {
            mbb.put(i, (byte) (i + 10));
        }
        System.out.println("mbb : position=" + mbb.position() + ",capacity=" + mbb.capacity() + ",limit=" + mbb.limit());
        System.out.println("magic=" + getMagicNum());
        System.out.println("createTime=" + getCreateTime());
        System.out.println("keyCapacity=" + getKeyCapacity());
        System.out.println("keyInserted=" + getKeyInserted());

        //mbb.position(BFI);
            /*bloomBuffer = mbb.asLongBuffer();
            System.out.println("bloomBuffer : position="+bloomBuffer.position()+",capacity="+bloomBuffer.capacity()+"," +
                    "limit="+bloomBuffer.limit());*/
        for (int i = BFI; i < mbb.limit(); i++) {
            System.out.println("bloom filter 第" + i + "个byte=" + mbb.get(i) + "," + Integer.toBinaryString(mbb.get(i)));
        }
    }

    private static byte getMagicNum() {
        return mbb.get(MI);
    }

    private static long getCreateTime() {
        return mbb.getLong(CTI);
    }

    private static int getKeyCapacity() {
        return mbb.getInt(KCI);
    }

    private static int getKeyInserted() {
        return mbb.getInt(KII);
    }

    private static boolean set(long index) {
        if (!get(index)) {
            index += META_NUM * Byte.SIZE;
            int bytePosition = (int) (index >>> 3);
            byte mod = (byte) (index & 0x07); // index mod 8
            final byte origin = mbb.get(bytePosition);
            final int newByte = origin | (byte) (1 << (8 - mod - 1));
            mbb.put(bytePosition, (byte) newByte);
            return true;
        }
        return false;
    }

    private static boolean get(long index) {
        index += META_NUM * Byte.SIZE;
        int bytePosition = (int) (index >>> 3);
        byte mod = (byte) (index & 0x07); // index mod 8
        return (mbb.get(bytePosition) & (byte) (1 << (8 - mod - 1))) != 0;
    }
    /*private static boolean set(long index) {
        if (!get(index)) {
            long value = bloomBuffer.get((int) (index >>> 6));
            value |= (1L << index);
            bloomBuffer.put((int) (index >>> 6), value);
            //bitSetCount++;
            return true;
        }
        return false;
    }

    private static boolean get(long index) {
        return (bloomBuffer.get((int) (index >>> 6)) & (1L << index)) != 0;
    }*/

    public static void main(String[] args) {
        initBuffer(System.currentTimeMillis(), 100, 89);


        Assert.assertTrue(get(3));
        Assert.assertTrue(get(4));
        Assert.assertFalse(get(5));
        Assert.assertTrue(get(6));
        Assert.assertTrue(get(7));
        Assert.assertTrue(get(11));
        Assert.assertTrue(get(12));
        Assert.assertTrue(get(13));

        Assert.assertFalse(get(0));
        Assert.assertFalse(get(1));
        Assert.assertFalse(get(2));
        Assert.assertFalse(get(8));
        Assert.assertFalse(get(14));


        Assert.assertEquals(27, mbb.get(17));
        if (set(5)) {
            Assert.assertTrue(get(5));
            Assert.assertEquals(31, mbb.get(17));
        }

    }
}
