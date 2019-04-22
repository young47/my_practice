package com.young.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

/**
 * 测试 映射文件改动，代码是否可以看见
 *
 * 若直接在硬盘删掉文件，再新建，则程序无法感知
 */
public class MmapTest {
    private static final File FILE = new File("/tmp/mmap.txt");

    public static void main(String[] args) throws IOException, InterruptedException {
        final RandomAccessFile file = new RandomAccessFile(FILE, "rw");
        final FileChannel channel = file.getChannel();
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 4);
        //map.putInt(0, 12345);
        Task task = new Task();
        new ReadThread(task).start();
        //new WriteThread(task).start();
    }

    private static class ReadThread extends Thread {
        private Task task;

        public ReadThread(Task task) {
            this.task = task;
        }

        @Override
        public void run() {
            while (true) {
                task.read();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class WriteThread extends Thread {
        private Task task;

        public WriteThread(Task task) {
            this.task = task;
        }

        @Override
        public void run() {
            while (true){
                task.write();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Task {
        private MappedByteBuffer rb;
        private MappedByteBuffer wb;
        private volatile boolean readable = true;

        public Task() {
            try {
                final RandomAccessFile file = new RandomAccessFile(FILE, "rw");
                final FileChannel channel = file.getChannel();
                wb = channel.map(FileChannel.MapMode.READ_WRITE, 0, 128);
                rb = channel.map(FileChannel.MapMode.READ_ONLY, 0, 128);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void write() {
            try {
                while (readable) {
                    wait();
                }
                wb.putInt(0, new Random().nextInt(1000));
                System.out.println("write : " + wb.getInt(0));
                readable = true;
                notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        public synchronized void read() {
            try {
                while (!readable) {
                    wait();
                }
                //readable = false;
                System.out.println("read : " + rb.getInt(0));
                notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
