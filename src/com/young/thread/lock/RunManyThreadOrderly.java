package com.young.thread.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by young on 17/12/26.
 */
public class RunManyThreadOrderly {

    public static void main(String[] args) {
        Task task = new Task();
        //new ProduceThread(task).start();
        for (int i = 0; i < 5; i++) {
            new ConsumeThread(task).start();
            new ProduceThread(task).start();
            //new ConsumeThread(task).start();
        }
    }

    static class Task {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition(); //其实也创建了条件队列 firstWaiter ---- lastWaiter
        private boolean isFull = false;

        public void produce() {
            lock.lock(); //如果没有抢到锁，则该线程会放入阻塞队列中
            //System.out.println("lock.getQueueLength(): "+lock.getQueueLength());
            try {
                while (isFull) {
                    condition.await();
                }
                isFull = true;
                condition.signalAll();
                System.out.println("※※※※※※※※※");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void consume() {
            lock.lock(); //如果没有抢到锁，则该线程会放入阻塞队列中
            //System.out.println("lock.getQueueLength(): "+lock.getQueueLength());
            try {
                while (!isFull) {
                    //System.out.println(Thread.currentThread().getName()+" is waiting");
                    condition.await();
                    //System.out.println(Thread.currentThread().getName()+" is waked up");
                }
                isFull = false;
                condition.signalAll();
                System.out.println(Thread.currentThread().getName()+" △△△△△△△");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    static class ProduceThread extends Thread {
        private Task task;

        ProduceThread(Task task) {
            this.task = task;
        }

        public void run() {
            task.produce();
        }
    }

    static class ConsumeThread extends Thread {
        private Task task;

        ConsumeThread(Task task) {
            this.task = task;
        }

        public void run() {
            task.consume();
        }
    }
}
