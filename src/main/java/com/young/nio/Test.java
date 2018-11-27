package com.young.nio;

import sun.nio.ch.DirectBuffer;
import sun.nio.ch.FileChannelImpl;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static com.young.jvm.OOM.StackOverflowTest.test;

/**
 * Created by young on 17/11/1.
 */
public class Test {
    /*
    JVM参数设置为： -Xmx1000m
     */
    static private File file = new File("/Users/young/Desktop/temp/bloom-filter.dump");
    static final int _1K = 1024, _1M = 1024 * 1024, _1G = _1M * 1024;
    static final int CAP = _1M * 1536;

    public static void main(String[] args) throws InterruptedException {
        //System.out.println(System.getProperty("sun.nio.MaxDirectMemorySize"));
        //commonIoTest();
        //commonIoLineTest();
        //nioTest();
        nioMappedBufferTest();
        //nioDirectMemoryTest();

    }

    /**
     * -XX:MaxDirectMemorySize=1280M
     */
    private static void nioDirectMemoryTest() {
        long start = System.currentTimeMillis();
        File target = new File("/Users/young/Desktop/temp/123.temp.bak");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(CAP);
        try {
            FileChannel channel = new FileInputStream(file).getChannel();

            FileChannel oc = new FileOutputStream(target).getChannel();
            while (channel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                oc.write(byteBuffer);
                byteBuffer.clear();
            }
            System.out.println("nio direct memory read finished!!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            long end = System.currentTimeMillis();
            System.out.println("nio direct memory cost : " + (end - start) + " ms");
        }

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        ((DirectBuffer) byteBuffer).cleaner().clean();
    }

    private static void nioMappedBufferTest() throws InterruptedException {
        //File directory = new File("/Users/young/Desktop/temp");
        MappedByteBuffer buff = null;
        while (true){
            try {
                if (!file.exists()){
                    file.createNewFile();
                }
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                FileChannel channel = raf.getChannel();
            /*
            如果文件大于2G，就会报错：Size exceeds Integer.MAX_VALUE, 所以得分开映射
             */
                //FileChannel oc = new FileOutputStream(target).getChannel();
            /*do {
            int num = (int) (channel.size() / (Integer.MAX_VALUE - 1));
            int i = 0;
                long pos = i * Integer.MAX_VALUE;
                long size = pos + Integer.MAX_VALUE - 1 > channel.size() ? channel.size() - pos : Integer.MAX_VALUE - 1;
                buff = channel.map(FileChannel.MapMode.READ_ONLY, pos, size);
                buff.flip();
                oc.write(buff, oc.position());
                oc.force(true);
                buff.clear();
                i++;
            } while (i < num);*/
                long start = System.currentTimeMillis();
                buff = channel.map(FileChannel.MapMode.READ_WRITE, 0, _1G);
                long end = System.currentTimeMillis();
                System.out.println("nio mapped memory cost : " + (end - start) + " ms");
                //oc.write(buff, oc.position());
                //MappedByteBuffer buff1 = channel.map(FileChannel.MapMode.READ_ONLY, Integer.MAX_VALUE, channel.size() - Integer.MAX_VALUE);
                //MappedByteBuffer buff1 = channel.map(FileChannel.MapMode.READ_ONLY, Integer.MAX_VALUE, Integer.MAX_VALUE - 1);
                //oc.write(buff1, oc.position());
                buff.put(0, (byte)0);
                Thread.sleep(70000);
                buff.put(0, (byte)0);
                System.out.println("begin to release memory");
                //buff = null;
                channel.close();
                //System.gc();
                /*Method m = FileChannelImpl.class.getDeclaredMethod("unmap",
                        MappedByteBuffer.class);
                m.setAccessible(true);
                m.invoke(FileChannelImpl.class, buff);
                System.out.println("release memory completely");*/

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                file.delete();
                Thread.sleep(120000);
                System.out.println("*********************");
            }
        }

    }

    private static void nioTest() {
        long start = System.currentTimeMillis();
        File target = new File("/Users/young/Movies/nio.mkv");
        FileChannel oc = null;
        try {
            FileChannel channel = new FileInputStream(file).getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(_1M * 600);
            oc = new FileOutputStream(target).getChannel();
            while (channel.read(byteBuffer) != -1) {
                byteBuffer.flip();//由写模式切换到读模式
                oc.write(byteBuffer);
                byteBuffer.clear();
            }
            System.out.println("nio read finished!!");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        } finally {
            long end = System.currentTimeMillis();
            System.out.println("nio cost : " + (end - start) + " ms");
            System.out.println(oc.isOpen());
        }
    }

    private static void commonIoTest() {
        long start = System.currentTimeMillis();
        File target = new File("/Users/young/Movies/commonIo.mkv");
        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(target);
            byte[] bytes = new byte[_1K];
            while (fis.read(bytes) != -1) {
                fos.write(bytes);
            }
            System.out.println("common io read finished!!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            long end = System.currentTimeMillis();
            System.out.println("common io cost : " + (end - start) + " ms");
        }
    }

    private static void commonIoLineTest() {
        long start = System.currentTimeMillis();
        File target = new File("/Users/young/Movies/commonIoBuffer.mkv");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(new FileOutputStream(target));
            byte[] bytes = new byte[_1K];
            while (bis.read(bytes) != -1) {
                bos.write(bytes);
            }
            System.out.println("common io buffer read finished!!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            long end = System.currentTimeMillis();
            System.out.println("common io buffer cost : " + (end - start) + " ms");
            try {
                bis.close();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
