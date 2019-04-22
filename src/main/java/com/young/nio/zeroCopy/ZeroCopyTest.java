package com.young.nio.zeroCopy;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ZeroCopyTest {
    private static String target = "/tmp/zeroCopy/target";
    private static String source = "/tmp/input.log";

    private static int num = 20;
    private static int _1M = 1024 * 1024;
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        commonTest();
        long e = System.currentTimeMillis();
        System.out.println((e - l) + "ms");

        zeroCopyTest();
        long e1 = System.currentTimeMillis();
        System.out.println((e1 - e) + "ms");
    }

    private static void zeroCopyTest() {
        try {
            FileChannel fromChannel = new RandomAccessFile(source, "r").getChannel();
            FileChannel toChannel;
            long position = 0, count = fromChannel.size();
            for (int i = 0; i < num; i++) {
                toChannel = new RandomAccessFile(target + "_zero_" + i + ".log", "rw").getChannel();
                fromChannel.transferTo(position, count, toChannel);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void commonTest() {
        try {
            FileChannel fromChannel = new RandomAccessFile(source, "r").getChannel();
            FileChannel toChannel;
            ByteBuffer byteBuffer = ByteBuffer.allocate((int)fromChannel.size());
            for (int i = 0; i < num; i++) {
                fromChannel.read(byteBuffer);
                toChannel = new RandomAccessFile(target + "_" + i + ".log", "rw").getChannel();
                toChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
