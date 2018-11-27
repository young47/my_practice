package com.young.thread.join;

/**
 * join()可以让几个线程按顺序执行
 * Created by young on 17/12/13.
 */
public class RunManyThreadOrderly {
    public static void main(String[] args) {
        Task task = new Task();
        try {
            for (int i = 1; i <= 10; i++) {
                MyThreadA myThreadA = new MyThreadA(task);
                myThreadA.start();
                myThreadA.join(); //阻塞main线程
                MyThreadB myThreadB = new MyThreadB(task);
                myThreadB.start();
                myThreadB.join();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static class Task {
        public void begin() {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("※※※※※※※※※");
        }

        public void end() {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("  △△△△△△△△△");
        }
    }

    static class MyThreadA extends Thread {
        private Task task;

        MyThreadA(Task task) {
            this.task = task;
        }

        @Override
        public void run() {
            task.begin();
        }
    }

    static class MyThreadB extends Thread {
        private Task task;

        MyThreadB(Task task) {
            this.task = task;
        }

        @Override
        public void run() {
            task.end();
        }
    }
}
