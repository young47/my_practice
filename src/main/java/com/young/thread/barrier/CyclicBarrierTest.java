package com.young.thread.barrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by young on 17/12/19.
 */
public class CyclicBarrierTest {
    public static void main(String[] args) throws Exception{
        int n = 3;
        CyclicBarrier cyclicbarrier = new CyclicBarrier(n, new Runnable(){
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+" 所有线程都到达栅栏处");
            }
        });
        for (int i=0; i<n-1; i++){
            new MyThread(i, cyclicbarrier).start();
        }
        Thread.sleep(3000);
        System.out.println("Over");
        cyclicbarrier.await();  //每次await()方法，等待的线程数就会减一

       /* for (int i=4; i<6; i++){
            new MyThread(i, cyclicbarrier).start();
        }
        Thread.sleep(3000);
        System.out.println("Over again");
        cyclicbarrier.await();*/
    }

    static class MyThread extends Thread {
        int i;
        CyclicBarrier barrier;

        MyThread(int i, CyclicBarrier barrier) {
            this.i = i;
            this.barrier = barrier;
        }

        public void run() {
            try {
                barrier.await();
                System.out.println("第" + i + "个线程到达");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
