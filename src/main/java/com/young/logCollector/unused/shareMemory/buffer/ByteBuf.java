package com.young.logCollector.unused.shareMemory.buffer;

import com.sohu.log.common.buffer.BufferManager;
import com.young.logCollector.unused.shareMemory.BufferedLog;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.concurrent.TimeoutException;

import static com.young.logCollector.unused.shareMemory.BufferedLog.NULL_LOG;

/**
 * meta file
 * +-------------------------------+
 * |            log  count         | 1 int
 * +-------------------------------+
 * |          write position       | 1 int  : byte为单位
 * +-------------------------------+
 * |           write index         | 1 int  : log为单位
 * +-------------------------------+
 * |           read position       | 1 int
 * +-------------------------------+
 * |            read index         | 1 int
 * +-------------------------------+
 * <p>
 * <p>
 * log file
 * +-------------------------------+
 * |           buffered log        | N bytes
 * +-------------------------------+
 * |           buffered log        | N bytes
 * +-------------------------------+
 * |           buffered log        | N bytes
 * +-------------------------------+
 * |           buffered log        | N bytes
 * +-------------------------------+
 * |           buffered log        | N bytes
 * +-------------------------------+
 * |   .........................   | N bytes
 * +-------------------------------+
 */
public class ByteBuf {
    private static final int metaSize = 20;
    private static final int LOCK_TIMEOUT_IN_MILLI = 10000;
    private static final byte EOF = (byte) 0XFF;
    //映射的文件
    private File logFile;
    //映射的开始位置
    private long mapBeginIndex;
    private long mapEndIndex;
    //映射的字节数
    private long mapSize;

    //两个buffer底层是同一堆数据，只是index不同
    private ByteBuffer writeBuffer;
    private ByteBuffer readBuffer;

    private Meta meta;
    private FileChannel fileChannel;
    private FileLock fileLock;


    private ByteBuf() {
    }

    public static ByteBuf allocate(File logFile, File metaFile, long logBegin, long logCap, String rw) throws Exception {
        ByteBuf logByteBuf = new ByteBuf();
        logByteBuf.meta = new Meta(BufferManager.map(new RandomAccessFile(metaFile, BufferManager.RW).getChannel(), metaFile
                .getName(), 0, metaSize, rw));
        logByteBuf.fileChannel = new RandomAccessFile(metaFile, "rw").getChannel();
        logByteBuf.writeBuffer = BufferManager.map(new RandomAccessFile(logFile, BufferManager.RW).getChannel(), logFile
                .getName(), logBegin, logCap, rw);
        logByteBuf.readBuffer = logByteBuf.writeBuffer.slice();
        logByteBuf.writeBuffer.position(logByteBuf.meta.writePos());
        logByteBuf.readBuffer.position(logByteBuf.meta.readPos());
        logByteBuf.logFile = logFile;
        logByteBuf.mapBeginIndex = logBegin;
        logByteBuf.mapSize = logCap;
        logByteBuf.mapEndIndex = logBegin + logCap - 1;
        return logByteBuf;
    }

    public boolean appendLog(BufferedLog log) {
        try {
            tryLock(LOCK_TIMEOUT_IN_MILLI);
            int size = log.size();
            if (isEof(meta.writePos(), size, true)) {
                //从头开始写
                System.out.println("write return to position 0, now pos = " + meta.writePos() + ", index = " + meta.writeIndex());
                System.out.println(this);
                if (mapEndIndex - meta.writePos() > BufferedLog.MIN_LENGTH) {
                    //若最后留下的空白太多，插入一个标记告诉read进程掉头;
                    //若留下的空白不多，read自己会判断
                    writeBuffer.put(EOF);
                }
                resetWritePos();
            }
            if (appendable(size)) {
                System.out.println("writing pos = " + meta.writePos() + ", index = " + meta.writeIndex());
                writeBuffer.put(log.toByte());
                updateMetaWrite();
                return true;
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (fileLock != null) {
                try {
                    fileLock.release();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public BufferedLog nextLog() {
        try {
            tryLock(LOCK_TIMEOUT_IN_MILLI);
            if (isEof(meta.readPos(), BufferedLog.MIN_LENGTH, false)) {
                System.out.println("read return to position 0, now pos = " + meta.readPos() + ", index = " + meta.readIndex());
                System.out.println(this);
                resetReadPos();
            }
            if (isReadable()) {
                //先读log的长度
                System.out.println("reading pos = " + meta.readPos() + ", index = " + meta.readIndex());
                int logSize = readBuffer.getInt(meta.readPos());
                byte[] log = BufferedLog.geneEmptyLog(logSize);
                readBuffer.get(log);
                updateMetaRead();
                return BufferedLog.getLog(log);
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (fileLock != null) {
                try {
                    fileLock.release();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return NULL_LOG;
    }

    private void updateMetaRead() {
        meta.readIndexInc();
        meta.readPos(readBuffer.position());
    }


    private void updateMetaWrite() {
        meta.logCountInc();
        meta.writeIndexInc();
        meta.writePos(writeBuffer.position());
    }

    private boolean isReadable() {
        //说明写得多，读得少
        return meta.readIndex() < meta.writeIndex();
    }

    private void resetReadPos() {
        meta.readPos(0);
        readBuffer.position(0);
    }


    private void resetWritePos() {
        meta.writePos(0);
        writeBuffer.position(0);
    }

    /**
     * 是否到了文件结尾
     *
     * @param pos
     * @param size
     * @param writing
     * @return
     */
    private boolean isEof(int pos, int size, boolean writing) {
        boolean bufferIsNotEnough = pos + size > mapEndIndex;
        if (writing) {
            return bufferIsNotEnough;
        } else {
            return bufferIsNotEnough | readBuffer.get(pos) == EOF;
        }
    }

    /**
     * @param size
     * @return
     */
    private boolean appendable(int size) {
        //pos相等的状态： 1.可能刚开始；2. 可能套圈了
        if (meta.readPos() == meta.writePos()) {
            return meta.writeIndex() <= meta.readIndex();
        } else {
            return meta.readPos() < meta.writePos() || meta.writePos() + size <= meta.readPos();
        }
    }

    public void release() {
        BufferManager.releaseBuffer(writeBuffer);
        BufferManager.releaseBuffer(readBuffer);
        BufferManager.releaseBuffer(meta.metaBuffer);
    }

    @Override
    public String toString() {
        return "ByteBuf{" +
                "mapBeginIndex=" + mapBeginIndex +
                ", mapEndIndex=" + mapEndIndex +
                ", mapSize=" + mapSize +
                ", meta=" + meta +
                '}';
    }

    public int logCount() {
        return meta.logCount();
    }

    public void flush() {
        BufferManager.flush(writeBuffer);
        BufferManager.flush(meta.metaBuffer);
    }

    public void close() {
        flush();
        try {
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tryLock(long timeoutInMills) throws TimeoutException {
        long deadLineTime = System.currentTimeMillis() + timeoutInMills;
        if (this.fileLock != null && this.fileLock.isValid()) {
            return;
        }
        do {
            try {
                this.fileLock = fileChannel.tryLock();
                if (this.fileLock != null) {
                    return;
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        } while (System.currentTimeMillis() < deadLineTime);

        throw new TimeoutException("Get Log Collector File Lock time out!");
    }

    private static class Meta {
        private static final int COUNT_POS = 0;
        private static final int WRITE_POS = 4;
        private static final int WRITE_INDEX = 8;
        private static final int READ_POS = 12;
        private static final int READ_INDEX = 16;


        private ByteBuffer metaBuffer;

        public Meta(ByteBuffer metaBuffer) {
            this.metaBuffer = metaBuffer;
        }

        @Override
        public String toString() {
            return "Meta{" +
                    "logCount=" + logCount() +
                    ", writePos=" + writePos() +
                    ", writeIndex=" + writeIndex() +
                    ", readPos=" + readPos() +
                    ", readIndex=" + readIndex() +
                    '}';
        }

        private int logCount() {
            return metaBuffer.getInt(COUNT_POS);
        }

        private int writePos() {
            return metaBuffer.getInt(WRITE_POS);
        }

        private int readPos() {
            return metaBuffer.getInt(READ_POS);
        }

        private int writeIndex() {
            return metaBuffer.getInt(WRITE_INDEX);
        }

        private int readIndex() {
            return metaBuffer.getInt(READ_INDEX);
        }

        private void readPos(int pos) {
            metaBuffer.putInt(READ_POS, pos);
        }

        private void writePos(int pos) {
            metaBuffer.putInt(WRITE_POS, pos);
        }

        private void logCountInc() {
            metaBuffer.putInt(COUNT_POS, metaBuffer.getInt(COUNT_POS) + 1);
        }

        public void writeIndexInc() {
            metaBuffer.putInt(WRITE_INDEX, metaBuffer.getInt(WRITE_INDEX) + 1);
        }

        public void readIndexInc() {
            metaBuffer.putInt(READ_INDEX, metaBuffer.getInt(READ_INDEX) + 1);
        }
    }
}
