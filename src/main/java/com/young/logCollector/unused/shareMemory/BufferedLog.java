package com.young.logCollector.unused.shareMemory;

import java.nio.ByteBuffer;

/**
 * BufferedLog 添加了头部：log的字节长度
 */
public class BufferedLog {
    private CollectedLog collectedLog;
    public int head;

    public static final int HEAD_SIZE = Integer.BYTES;

    //一条buffered log最短长度 = 4字节的头 + 1字节的信息
    public static final int MIN_LENGTH = HEAD_SIZE + 1;

    public static final BufferedLog NULL_LOG = new BufferedLog();

    private BufferedLog(){}

    public BufferedLog(CollectedLog collectedLog) {
        this.collectedLog = collectedLog;
        this.head = collectedLog.size();
    }

    public int size() {
        return collectedLog.size() + HEAD_SIZE;
    }

    public byte[] toByte() {
        ByteBuffer buffer = ByteBuffer.allocate(size());
        buffer.putInt(head);
        buffer.put(collectedLog.getLog());
        return buffer.array();
    }

    public static BufferedLog getLog(byte[] bytes) {
        byte[] dest = new byte[bytes.length - HEAD_SIZE];
        System.arraycopy(bytes, HEAD_SIZE, dest, 0, dest.length);
        return new BufferedLog(new CollectedLog(dest));
    }

    public static byte[] geneEmptyLog(int logSize) {
        return new byte[logSize + HEAD_SIZE];
    }

    public String logInfo() {
        return collectedLog.logInfo();
    }

    public int getHead() {
        return head;
    }
}
