package com.young.nio;

import sun.nio.ch.FileChannelImpl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static com.young.nio.Test._1G;

public class ClearMappedMemory {
    static private File file = new File("/Users/young/Desktop/temp/bloom-filter.dump");
    public static void main(String[] args) throws IOException, InterruptedException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        clearMemory();
    }

    private static void clearMemory() throws IOException, InterruptedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (!file.exists()){
            file.createNewFile();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        FileChannel channel = raf.getChannel();
        MappedByteBuffer buff = channel.map(FileChannel.MapMode.READ_WRITE, 0, _1G);
        Thread.sleep(10000);
        Method m = FileChannelImpl.class.getDeclaredMethod("unmap",
                MappedByteBuffer.class);
        m.setAccessible(true);
        m.invoke(FileChannelImpl.class, buff);
        System.out.println("clear release memory completely");

    }
}
