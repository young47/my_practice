package com.young.thread;

/**
 * Created by young on 17/11/23.
 */
public class LockTest {
    public static void main(String[] args) {
        //testVisible();
        testWait();

    }

    private static void testVisible() {
        try {
            Task task = new Task();
            new Thread(task).start();
            Thread.sleep(1000);
            System.out.println("Main Thread want to stop another Thread");
            task.setFlag(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void testWait() {
        try {
            String s = new String();
            s.wait();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    static class Task implements Runnable {
        private boolean flag = true;

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        @Override
        public void run() {
            try {
                while (flag) {
                    System.out.println("Sub Thread Name : " + Thread.currentThread().getName());
                    //Thread.sleep(1);
                }
            }/* catch (InterruptedException e) {
                e.printStackTrace();
            }*/ catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
