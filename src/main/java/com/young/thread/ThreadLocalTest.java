package com.young.thread;

import org.apache.commons.lang.StringUtils;

public class ThreadLocalTest {


    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new ThreadLocal<String>() {
            @Override
            protected String initialValue() {
                return Thread.currentThread().getName();
            }
        };
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    String s = threadLocal.get();
                    if (StringUtils.isBlank(s)) {
                        System.out.println("set value in threadLocal");
                        threadLocal.set("subThread");
                    }
                    System.out.println("value="+s);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.setName("subThread");
        thread.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadLocal.remove();

    }
}
