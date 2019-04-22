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
                while (isFull) { //这里注意不要使用if，因为在现代多核cpu系统中，某些时候一个等待的线程会无缘无故地被唤醒，为了避免这种情况，请用while
                    //System.out.println(Thread.currentThread().getName()+" is waiting");
                    wait();
                    //System.out.println(Thread.currentThread().getName()+" is waked up");
                }
                isFull = true; //synchronized内，这个变量会有volatile的效果
                notify();
                System.out.println("※※※※※※※※※");
                /*while (!isFull) { //这里注意不要使用if，因为在现代多核cpu系统中，某些时候一个等待的线程会无缘无故地被唤醒，为了避免这种情况，请用while
                    System.out.println("※※※※※※※※※");
                    isFull = true; //synchronized内，这个变量会有volatile的效果
                    notify();
                }
                wait();*/
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
                /*while (isFull) {
                    System.out.println("△△△△△△△");
                    isFull = false;
                    notify();
                }
                wait();*/
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
