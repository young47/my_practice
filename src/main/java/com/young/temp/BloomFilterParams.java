package com.young.temp;

public final class BloomFilterParams {

    /**
     * 目前每天的(cid,oid)数量量大概：
     *    click: <=1000万
     *    ltr: ≈2亿（2000万 * 10）
     *    local: <=1亿
     *    topic: <=1亿
     *    快讯: ≈2亿（2000万 * 10）
     *
     */
    public static final int MAX_KEY_INSERTED = 2000_0000; //5亿的量目前可以容下每天的量


    //内存映射文件最大为2G左右，因此设置这两个参数时要考虑这一点
    public static final double FPP = 0.0001d; //这个错误率(1/10000)下，5亿的磁盘文件大概1G

    //magic num
    public static final byte MAGIC = (byte) 0xBC;

    //magic num index
    public static final int MI = 0;

    //create time index
    public static final int CTI = 1;

    //key capacity index
    public static final int KCI = 9;

    //key inserted index
    public static final int KII = 13;

    //1-bit num index
    public static final int OBCI = 17;

    //meta num of bytes
    public static final int META_NUM = 21;

    //bloom filter index
    public static final int BFI = META_NUM;
}
