package com.young.thread.semphore;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by young on 17/12/25.
 */
public class SemaphoreTest {
    private static final int THREAD_COUNT = 30;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
    private static Semaphore semaphore = new Semaphore(10, false);

    /**
     * 同时起了30个线程，但每次只让运行10个
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            int j = i;
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println(Thread.currentThread().getName() + " ,j=" + j);

                        Thread.sleep(new Random().nextInt(3000));

                        semaphore.release();
                    } catch (InterruptedException e) {

                    }
                }
            });
        }
        threadPool.shutdown();
    }
}
