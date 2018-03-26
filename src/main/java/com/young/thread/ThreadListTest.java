package com.young.thread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengyang215509 on 2018/3/23.
 */
public class ThreadListTest {
    public static void main(String[] args){
        List<String> list = new ArrayList<String>();
        for (int i=1; i<=10; i++){
            new Thread1(list).start();
        }

    }

    static class Thread1 extends Thread {
        private List<String> list;
        public Thread1(List<String> list){
            this.list = list;
        }
        @Override
        public void run() {
            list.add(System.currentTimeMillis()+"");
            System.out.println("list size : "+ list.size());
        }
    }
}
