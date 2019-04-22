package com.young.interview;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 两个线程交替打印1到100
 */
public class ThreadTest {
    public static void main(String[] args) throws InterruptedException {
        //method 1
        //method1();

        //method 2
        //method2();
        //
        method3();
    }

    private static void method3() {
        Task2 task = new Task2();
        new ThreadD(task, "odd").start();
        new ThreadC(task, "even").start();
    }

    private static void method2() {
        try {
            for (int i = 1; i <= 100; i = i + 2) {
                final int j = i;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName() + ": " + j);
                    }
                });
                thread.setName("odd");
                thread.start();
                thread.join();

                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName() + ": " + (j + 1));
                    }
                });
                thread2.setName("even");
                thread2.start();
                thread2.join();
            }
        } catch (Exception e) {

        }
    }

    private static void method1() {
        Task task = new Task();
        new ThreadA(task, "odd").start();//odd
        new ThreadB(task, "even").start();//even
    }

    static class Task {

        private boolean even = false;

        private int num = 1;

        public void printEven() {
            try {
                while (num <= 100) {
                    synchronized (this) {
                        while (!even) {
                            wait();
                        }
                        System.out.println(Thread.currentThread().getName() + ": " + num++);
                        even = false;
                        notify();
                    }
                }
            } catch (Exception e) {

            }

        }

        public void printOdd() {
            try {
                while (num < 100) {
                    synchronized (this) {
                        while (even) {
                            wait();
                        }
                        System.out.println(Thread.currentThread().getName() + ": " + num++);
                        even = true;
                        notify();
                    }
                }

            } catch (Exception e) {

            }
        }
    }

    static class ThreadA extends Thread {
        private Task task;

        public ThreadA(Task task, String name) {
            this.task = task;
            this.setName(name);
        }

        @Override
        public void run() {
            task.printEven();
        }
    }

    static class ThreadB extends Thread {
        private Task task;

        public ThreadB(Task task, String name) {
            this.task = task;
            this.setName(name);
        }

        @Override
        public void run() {
            task.printOdd();
        }
    }

    static class ThreadC extends Thread {
        private Task2 task;

        public ThreadC(Task2 task, String name) {
            this.task = task;
            this.setName(name);
        }

        @Override
        public void run() {
            task.printEven();
        }
    }

    static class ThreadD extends Thread {
        private Task2 task;

        public ThreadD(Task2 task, String name) {
            this.task = task;
            this.setName(name);
        }

        @Override
        public void run() {
            task.printOdd();
        }
    }

    static class Task2 {

        private int num = 1;

        private ReentrantLock lock = new ReentrantLock();
        private Condition printEven = lock.newCondition();
        private Condition printOdd = lock.newCondition();
        private boolean canPrintEven = false;

        public void printEven() {
            while (num <= 100) {
                try {
                    lock.lock();
                    while (!canPrintEven) {
                        printEven.await();//偶数先等着
                    }
                    System.out.println(Thread.currentThread().getName() + ": " + num++);
                    canPrintEven = false;
                    printEven.signalAll();
                } catch (Exception e) {

                } finally {
                    lock.unlock();
                }

            }


        }

        public void printOdd() {
            while (num < 100) {
                try {
                    lock.lock();
                    while (canPrintEven) {
                        printEven.await();
                    }
                    System.out.println(Thread.currentThread().getName() + ": " + num++);
                    canPrintEven = true;
                    printEven.signalAll();//通知打印偶数
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

            }
        }
    }
}
