package com.young.temp;

/**
 * bloom filter包含4个重要的参数
 * 1. bitNums: bit位的个数
 * 2. keyCapacity: 能放入的最大的元素；若超过这一值，错误率会飙升
 * 3. hashNums: hash函数的个数
 * 4. fpp: false positive probability，误报率
 * @param <T>
 */
public interface BloomFilter<T> {
    /**
     *  bit位的总个数
     * @return
     */
    long getBitNums();

    int getKeyCapacity();

    int getKeyInserted();

    short getHashNums();

    double getFpp();

    /**
     * 在保证错误率的情况下，还能插入多少个key
     * @return
     */
    long getRemaining();

    /**
     *  设为1的bit个数
     * @return
     */
    long getOneBitCount();

    boolean mightContain(T t);

    boolean put(T t);

    boolean clear();
}
