package com.young.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CacheTest {
    private static final Cache<String, String> cache = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.SECONDS)
            .recordStats().maximumSize(10).build();

    public static void main(String[] args) {
        //cache.put("key", "oldValue");
        for (int q = 0; q < 5; q++) {
            new Thread(new Task(), "thread-" + q).start();
        }
    }

    static class Task implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    String value = cache.get("key", new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            return null;
                        }
                    });
                    /*if (StringUtils.isBlank(value)){
                        throw new RuntimeException("123456");
                    }*/
                    System.out.println(Thread.currentThread().getName() + " getValue = " + value);
                    //Thread.sleep(200);
                }  catch (ExecutionException e) {
                    System.out.println("------------"+Thread.currentThread().getName());
                    e.printStackTrace();
                }
            }

        }
    }

}
