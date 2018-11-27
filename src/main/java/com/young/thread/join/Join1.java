package com.young.thread.join;

/**
 * Created by young on 17/12/14.
 */
public class Join1 {

    /**
     * 运行结果可能包含以下：
     * 第一种：
         MyThreadA Begin...
         MyThreadA End...
         MyThreadB Begin...
         MyThreadB End...
         Main End...
     第二种：
         MyThreadA Begin...
         MyThreadA End...
         Main End...
         MyThreadB Begin...
         MyThreadB End...
     第三种：
        MyThreadB Begin...1538190438984
        MyThreadA Begin...1538190438984
        MyThreadA End...1538190440985
        Main End...1538190440986
        MyThreadB End...1538190441988
     * @param args
     *
     * join(int milli) : milli表示当前线程至多wait这么长时间，时间一过，当前线程就会与别的线程争抢执行时间片。
     */
    public static void main(String[] args) {
        try {
            for (int i=1; i<=1; i++) {
                MyThreadB myThreadB = new MyThreadB();
                MyThreadA myThreadA = new MyThreadA(myThreadB);
                myThreadA.start(); //A线程会尝试获取 myThreadB 的锁
                myThreadB.start(); //因为A获取了 myThreadB 的锁，所以 B线程会等待
                myThreadB.join(1000); //与此同时，main线程也会去尝试获取 myThreadB 的锁，但只会wait 1s,在此期间，main线程是释放锁，并阻塞的状态。1s之后，main线程醒来，与A，B线程去争抢执行时机。谁抢到了，谁就会先运行。
                //myThreadB.join(4000);
                System.out.println("Main End..." + System.currentTimeMillis()); //所以，这句可能先于B线程运行。
                //System.out.println("  ----  ");
                //join()方法会使main线程去尝试获得锁，这与A线程的运行几乎是同时开始的。
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    static class MyThreadA extends Thread {
        private MyThreadB threadB;

        MyThreadA(MyThreadB threadB) {
            this.threadB = threadB;
        }

        @Override
        public void run() {
            try {
                synchronized (threadB) {
                    System.out.println("MyThreadA Begin..."+System.currentTimeMillis());
                    sleep(2000);
                    System.out.println("MyThreadA End..."+System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class MyThreadB extends Thread {

        @Override
        public void run() {
            try {
                System.out.println("MyThreadB Begin..."+System.currentTimeMillis());
                sleep(3000);
                System.out.println("MyThreadB End..."+System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
