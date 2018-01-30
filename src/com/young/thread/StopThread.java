package com.young.thread;

/**
 * Created by young on 17/11/15.
 */
public class StopThread {
    public static void main(String[] args) {
        //stop thread test
       /* try {
            MyThread myThread = new MyThread();
            myThread.start();
            Thread.sleep(50);
            myThread.interrupt();//中断子线程
        } catch (InterruptedException e) {
            System.out.println("main catch");
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }*/
        /*MyThread myThread = new MyThread();
        myThread.start();
        myThread.interrupt();//中断子线程*/

        //yield test
        YieldThread yieldThread = new YieldThread();
        yieldThread.start();
    }
}

class MyThread extends Thread {
    @Override
    public void run() {
        //1，运行时停止线程
        /*for (int i = 0; i < 500000; i++) {
            if (Thread.interrupted()) {
                System.out.println("MyThread 停止");
                //break;
                return;
            }
            System.out.println("i = "+i);
        }
        System.out.println("MyThread Over");*/

        //2, sleeping时停止线程,依然会InterruptedException
        // 正在sleeping的线程被interrupt会抛InterruptedException并清掉中断标志
        // 如果一个线程先打上了中断标志后，调用sleep，也会抛InterruptedException并清掉中断标志
        try {
            for (int i = 0; i < 500000; i++) {
                System.out.println("i = " + i);
                if (this.isInterrupted()){
                    System.out.println("MyThread 停止");
                    break;
                }
            }
            System.out.println("this.isInterrupted() : "+this.isInterrupted());
            System.out.println("sleep begin");
            Thread.sleep(1000);
            System.out.println("sleep end");
        } catch (InterruptedException e) {
            System.out.println("aaa " + this.isInterrupted());
            e.printStackTrace();
        }

    }
}

class YieldThread extends Thread {
    @Override
    public void run() {
        long count = 0l;
        long begin = System.currentTimeMillis();
        for (int i=0; i<10000000; i++){
            //Thread.yield();
            //System.out.println("i = "+i);
            count = count + i;
        }
        long end = System.currentTimeMillis();
        System.out.println("cost time : "+(end-begin)+"ms");
    }
}


