package com.young.thread.interrupt;

public class InterruptTest {

    public static void main(String[] args) {
        final Thread thread = new Thread("interrupt-thread"){
            @Override
            public void run() {
                int j = 0;
                for (int i = 0; i < 100000; i++) {
                    j++;
                }
                System.out.println("j="+j);
                 /*try {
                   sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("interrupted");
                }*/
                if (Thread.currentThread().isInterrupted()){
                    System.out.println(Thread.currentThread().isInterrupted());
                    Thread.currentThread().interrupt();
                    return;
                }
                j = 0;
                for (int i = 0; i < 100000; i++) {
                    j++;
                }
                System.out.println("j="+j);
            }
        };

        thread.start();

        thread.interrupt();
    }
}
