package com.young.temp;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.young.temp.BloomFilterParams.*;

public final class PushBloomFilter implements BloomFilter<String> {

    private int keyCapacity;
    private final double fpp;
    private final int hashNums;
    private final long bitNums;
    private final long byteNums;
    private long createTime = System.currentTimeMillis();
    //设置为1的bit数量
    private AtomicLong oneBitCount = new AtomicLong(0);
    //todo: 这个值还没有设置到sets中
    private AtomicInteger keyInserted = new AtomicInteger(0);
    //bitmap
    private final Bits bits = new Bits();

    public static PushBloomFilter create(int keyCapacity, double fpp, File dumpFile) {
        return new PushBloomFilter(keyCapacity, fpp, dumpFile);
    }

    private PushBloomFilter(int keyCapacity, double fpp, File dumpFile) {
        this.keyCapacity = keyCapacity;
        this.fpp = fpp;
        this.bitNums = optimalNumsOfBit(keyCapacity, fpp);
        this.hashNums = optimalNumOfHashFunctions(keyCapacity, this.bitNums);
        this.byteNums = ((bitNums + 8) >> 3);
        bits.allocate(dumpFile, (int) byteNums, createTime, keyCapacity);
    }

    public boolean filterIsValid() {
        return bits.getMagicNum() == MAGIC;
    }

    @Override
    public long getBitNums() {
        return bitNums;
    }

    @Override
    public int getKeyCapacity() {
        return keyCapacity;
    }

    @Override
    public short getHashNums() {
        return (short) hashNums;
    }

    @Override
    public double getFpp() {
        return fpp;
    }

    @Override
    public long getRemaining() {
        return keyCapacity - keyInserted.get();
    }

    @Override
    public long getOneBitCount() {
        return oneBitCount.get();
    }

    public long getCreateTime() {
        return createTime;
    }

    @Override
    public int getKeyInserted() {
        return keyInserted.get();
    }

    @Override
    public boolean clear() {
        return bits.clearBuffer();
    }

    @Override
    public boolean mightContain(String s) {
        byte[] bytes = Hashing.murmur3_128().hashString(s, Charsets.UTF_8).asBytes();
        long hash1 = lowerEight(bytes);
        long hash2 = upperEight(bytes);

        long combinedHash = hash1;
        for (int i = 0; i < hashNums; i++) {
            // Make the combined hash positive and indexable
            if (!bits.get((combinedHash & Long.MAX_VALUE) % bitNums)) {
                return false;
            }
            combinedHash += hash2;
        }
        return true;
    }

    @Override
    public boolean put(String s) {
        byte[] bytes = Hashing.murmur3_128().hashString(s, Charsets.UTF_8).asBytes();
        long hash1 = lowerEight(bytes);
        long hash2 = upperEight(bytes);

        boolean bitsChanged = false;
        long combinedHash = hash1;
        for (int i = 0; i < hashNums; i++) {
            // Make the combined hash positive and indexable
            bitsChanged |= bits.set((combinedHash & Long.MAX_VALUE) % bitNums);
            combinedHash += hash2;
        }
        keyInserted.incrementAndGet();
        return bitsChanged;
    }

    private static long optimalNumsOfBit(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    private static int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round(m / n * Math.log(2)));
    }

    private long lowerEight(byte[] bytes) {
        return Longs.fromBytes(
                bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
    }

    private long upperEight(byte[] bytes) {
        return Longs.fromBytes(
                bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
    }

    /*************************************************************************************************************************/

    /**
     * Bits的结构如下：
     * 除了bloom filter外，头部插入了若干个byte的meta info
     * +-------------------------------+
     * |           magic num           | 1 byte
     * +-------------------------------+
     * |          create time          | 1 long
     * +-------------------------------+
     * |          key capacity         | 1 int
     * +-------------------------------+
     * |          key inserted         | 1 int
     * +-------------------------------+
     * |           1-bit num           | 1 long
     * +-------------------------------+
     * |            bloom              | 1 byte
     * +-------------------------------+
     * |            bloom              | 1 byte
     * +-------------------------------+
     * |   .........................   | 1 byte
     * +-------------------------------+
     */
    private class Bits {
        private File DUMP_FILE;
        private static final String RW_MODE = "rw";
        private MappedByteBuffer mbb;

        /**
         * @param dumpFile
         * @param byteNums
         * @param createTime
         * @param keyCapacity
         * @return
         */
        private boolean allocate(File dumpFile, int byteNums, long createTime, int keyCapacity) {
            this.DUMP_FILE = dumpFile;
            try {
                if (!dumpFile.exists()) {
                    dumpFile.createNewFile();
                }
                final RandomAccessFile file = new RandomAccessFile(dumpFile, RW_MODE);
                boolean initNeeded = ((byte) file.read()) != MAGIC;
                int byteNumContainingMetaInfo = byteNums + META_NUM;
                if (byteNumContainingMetaInfo > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("Params is invalid! Maybe keyCapacity is too large or fpp is too small!");
                }
                this.mbb = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, byteNumContainingMetaInfo);
                initBufferIfNeeded(createTime, keyCapacity, initNeeded);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        private void initBufferIfNeeded(long createTime, int keyCapacity, boolean initNeeded) {
            if (initNeeded) {
                mbb.put(MAGIC);
                mbb.putLong(CTI, createTime);
                mbb.putInt(KCI, keyCapacity);
                mbb.putInt(KII, 0);
                mbb.putLong(OBCI, 0);
                for (int i = BFI; i < mbb.limit(); i++) {
                    mbb.put(i, (byte) 0);
                }
                System.out.println("Create new bloom filter");
            } else {//从文件中读取数据
                PushBloomFilter.this.createTime = mbb.getLong(CTI);
                PushBloomFilter.this.keyCapacity = mbb.getInt(KCI);
                PushBloomFilter.this.keyInserted = new AtomicInteger(mbb.getInt(KII));
                PushBloomFilter.this.oneBitCount = new AtomicLong(mbb.getLong(OBCI));
                System.out.println("Init bloom filter according to file : " + DUMP_FILE);
            }
        }

        private boolean set(long index) {
            if (!get(index)) {
                index += META_NUM * Byte.SIZE;
                int bytePosition = (int) (index >> 3);
                byte mod = (byte) (index & 0x07); // index mod 8
                final byte origin = mbb.get(bytePosition);
                final int newByte = origin | (byte) (1 << (8 - mod - 1));
                mbb.put(bytePosition, (byte) newByte);
                PushBloomFilter.this.oneBitCount.incrementAndGet();
                return true;
            }
            return false;
        }

        private boolean get(long index) {
            index += META_NUM * Byte.SIZE;
            int bytePosition = (int) (index >> 3);
            byte mod = (byte) (index & 0x07); // index mod 8
            return (mbb.get(bytePosition) & (byte) (1 << (8 - mod - 1))) != 0;
        }

        private byte getMagicNum() {
            return mbb.get(0);
        }

        //todo: below not really clear
        private boolean clearBuffer() {
            for (int i = 0; i < mbb.capacity(); i++) {
                mbb.put(i, (byte) 0);
            }
            return true;
        }
    }
}
