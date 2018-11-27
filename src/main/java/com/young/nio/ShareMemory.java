package com.young.nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ShareMemory {
    private static final File FILE = new File("/Users/young/Desktop/temp/bloom-filter.dump");
    private static int size = 1<<20;
    private static MappedByteBuffer sharedBuffer = null;
    private static final int RF = 1;
    private static final int WF = 0;

    static {
        try {
            final RandomAccessFile file = new RandomAccessFile(FILE, "rw");
            final FileChannel channel = file.getChannel();
            System.out.println("Before write buffer : "+Runtime.getRuntime().freeMemory());
            sharedBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, size);
            System.out.println("After write buffer : "+Runtime.getRuntime().freeMemory());

            //清除文件内容 ，对 MappedByteBuffer 的操作就是对文件的操作
            for(int i=0;i<size;i++){
                sharedBuffer.put(i,(byte)0);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //从第3个字节开始写入A-Z字母
        for (int i = 65; i <= 90; i++) {
            int index = i - 63;
            int flag = sharedBuffer.get(0);
            if (flag != WF){
                System.out.println("当前有程序在读，不能写");
                i --;
                Thread.sleep(1000);
                continue;
            }
            sharedBuffer.put(1, (byte)index);//正在写的位置
            sharedBuffer.put(index, (byte)i);
            System.out.println(System.currentTimeMillis()+" position:"+index+" 写入了:"+(char)i);
            sharedBuffer.put(0, (byte)RF);

            Thread.sleep(3000);
        }
    }
}
