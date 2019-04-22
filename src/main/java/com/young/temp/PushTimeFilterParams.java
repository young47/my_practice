package com.young.temp;

public final class PushTimeFilterParams {
    public static final int _1MIN = 60 * 1000;

    public static final int _1HOUR = 60 * _1MIN;

    public static final int _1DAY = 24* 60 * _1MIN;

    public static final int PUSH_TIME_VALID_SPAN_IN_MILLS = 1 * _1DAY;

    /** dump file */
    public static final String DUMP_FILE = "/tmp/test/push-time.dump";
    /** 每个map里保存的keys量 */
    public static final int KEYS_PER_MAP = 300_0000;

    public static final int LAST_MAP_AVAILABLE_PERCENT = 30;

    /** dump时，是否压缩 */
    public static final boolean GZIP_COMPRESS = false;

    public static final Long KEY_REMOVED_FLAG = new Long(0);
    public static final Long EXPIRED = new Long(0);
    /** 映射文件中元数据的字节大小 */
    public static final int META_NUM = 8;

    public static final int K_BYTES = Long.BYTES;
    public static final int V_BYTES = Long.BYTES;
    /** pushTimeMap中一个key和value占据的字节大小*/
    public static final int KV_BYTES = K_BYTES + V_BYTES;


    /** meta info */
    public static final byte MAGIC = (byte) 0xCC;

    //create time index
    public static final int CTI = 1;

    //cid nums index
    public static final int CNI = 9;

    //pushtime begin index in byte
    public static final int PTBI = 21;

}
