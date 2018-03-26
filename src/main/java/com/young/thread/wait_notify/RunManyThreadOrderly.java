package com.young.thread.wait_notify;

/**
 * Created by young on 17/12/13.
 */
public class RunManyThreadOrderly {
    public static void main(String[] args) {
        Task task = new Task();
        for (int i = 1; i <= 10; i++) {
            new MyThreadB(task).start();
            new MyThreadA(task).start();
        }
    }

    static class Task {
        private boolean isFull = false;

        public synchronized void produce() {
            try {
                while (isFull) {
                    //System.out.println(Thread.currentThread().getName()+" is waiting");
                    wait();
                    //System.out.println(Thread.currentThread().getName()+" is waked up");
                }
                isFull = true; //synchronized内，这个变量会有volatile的效果
                notify();
                System.out.println("※※※※※※※※※");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public synchronized void consume() {
            try {
                while (!isFull) {
                    //System.out.println(Thread.currentThread().getName()+" is waiting");
                    wait();
                    //System.out.println(Thread.currentThread().getName()+" is waked up");
                }
                isFull = false;
                notify();
                System.out.println("△△△△△△△");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class MyThreadA extends Thread {
        private Task task;

        MyThreadA(Task task) {
            this.task = task;
        }

        @Override
        public void run() {
            task.produce();
        }
    }

    static class MyThreadB extends Thread {
        private Task task;

        MyThreadB(Task task) {
            this.task = task;
        }

        @Override
        public void run() {
            task.consume();
        }
    }
}
