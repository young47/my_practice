package com.young.nio;

import sun.nio.ch.FileChannelImpl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 * 测试 内存映射，同一个文件会打开几个fd
 *
 * filter重构中，发现同一个dump会打开多个fd
 *
 * lsof 或 /proc/pid/fd 可以查看
 */
public class MmapTest2 {
    private static final File FILE = new File("/tmp/mmap.txt");
    public static void main(String[] args) throws IOException, InterruptedException {
        final RandomAccessFile file = new RandomAccessFile(FILE, "rw");
        final FileChannel channel = file.getChannel();
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 4);
        map.putInt(0, 12345);
        Thread.sleep(30000);
        unmap2(map);
        System.out.println("释放了map");
        Thread.sleep(30000);
        /*channel.close();
        System.out.println("关闭了channel");*/

    }

    private static boolean unmap(ByteBuffer byteBuffer) {
        try {
            Method m = FileChannelImpl.class.getDeclaredMethod("unmap", MappedByteBuffer.class);
            m.setAccessible(true);
            m.invoke(FileChannelImpl.class, byteBuffer);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean unmap2(ByteBuffer byteBuffer) {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    Method getCleanerMethod = byteBuffer.getClass().getMethod("cleaner");
                    getCleanerMethod.setAccessible(true);
                    Object cleaner = getCleanerMethod.invoke(byteBuffer); // sun.misc.Cleaner instance
                    Method cleanMethod = cleaner.getClass().getMethod("clean");
                    cleanMethod.invoke(cleaner);
                    return null;
                }
            });
            return true;
        } catch (PrivilegedActionException e) {
            e.printStackTrace();
            return false;
        }
    }
}
