package com.young.thread;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TooManyThreadTest {
    private static final int num = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(num, 2*num, 10, TimeUnit.MINUTES, new
            ArrayBlockingQueue<>(100));

    private static void main(String[] args){
        final ArrayList<String> list = new ArrayList<>(1000_0000);
        for (int i = 0; i < 1000_0000; i++) {
            list.add(i+"");
        }

        EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
