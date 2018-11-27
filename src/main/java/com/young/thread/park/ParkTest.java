package com.young.thread.park;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by young on 17/12/18.
 */
public class ParkTest {
    public static void main(String[] args){

        //检查park是否能响应interrupt
        testParkWhenInterrupt();
    }

    private static void testParkWhenInterrupt() {
        final Task task = new Task();
        final Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(task, "thread-"+i);
            threads[i].start();
        }
        threads[9].interrupt();
    }

    static class Task implements Runnable{
        ReentrantLock lock = new ReentrantLock();
        @Override
        public void run() {
            try {
                lock.lockInterruptibly();//在这里park的线程才能响应interrupt

                //获取到锁的线程根本就不理会interrupt
                int i = 0;
                while (i++<10000000){

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(Thread.currentThread().getName() + " is interrupted.");
            } finally {
                lock.unlock();
            }
        }
    }
}
