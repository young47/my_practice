package com.young.nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ByteBufferTest {
    private static final File FILE = new File("/tmp/test/byte-buffer-test.dump");

    public static void main(String[] args) throws IOException {
        final RandomAccessFile file = new RandomAccessFile(FILE, "rw");
        final FileChannel channel = file.getChannel();
        MappedByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 10);
        byteBuffer.put(9, (byte)9);
        //byteBuffer.put(10, (byte)10);
        //byteBuffer.put(11, (byte)11);

        MappedByteBuffer byteBuffer1 = channel.map(FileChannel.MapMode.READ_WRITE, 10, 10);
        byteBuffer1.put(10, (byte)10);

    }

}
