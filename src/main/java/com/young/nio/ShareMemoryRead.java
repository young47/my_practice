package com.young.nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ShareMemoryRead {
    private static final File FILE = new File("/Users/young/Desktop/temp/bloom-filter.dump");
    private static int size = 1<<20;
    private static MappedByteBuffer sharedBuffer = null;
    private static final int RF = 1;
    private static final int WF = 0;
    static {
        try {
            final RandomAccessFile file = new RandomAccessFile(FILE, "rw");
            final FileChannel channel = file.getChannel();
            System.out.println("Before read buffer : "+Runtime.getRuntime().freeMemory());
            sharedBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, size);
            System.out.println("After read buffer : "+Runtime.getRuntime().freeMemory());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int lastIndex = 0;
        for (int i = 1; i <= 26; i++) {
            final byte flag = sharedBuffer.get(0);
            int index = sharedBuffer.get(1);
            if (flag != RF || index == lastIndex) {
                System.out.println("当前有程序在写，不能读");
                i--;
                Thread.sleep(2000);
                continue;
            }
            lastIndex = index;
            System.out.println(System.currentTimeMillis()+" position:"+index+" 读入了:"+(char)sharedBuffer.get(index));
            sharedBuffer.put(0, (byte)WF);

            Thread.sleep(4000);
            if (index == 27){
                break;
            }
        }
    }
}
