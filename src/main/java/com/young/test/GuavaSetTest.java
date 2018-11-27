package com.young.test;

import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.Set;

public class GuavaSetTest {
    private static Set<String> set = Sets.newConcurrentHashSet();

    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            set.add(i+"");
        }
        System.out.println(set);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Iterator<String> iterator = set.iterator();
                for(iterator=set.iterator(); iterator.hasNext(); ){
                    String item = iterator.next();
                    if("4".equals(item)) {
                        try {
                            System.out.println("hahaha");
                            Thread.sleep(10000);
                            System.out.println("hahaha hahaha");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("now set = "+ set);
                        if (set.contains("4")){
                            System.out.println(item+" 88888");
                        }
                        iterator.remove();

                    }
                }
                System.out.println(set);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Iterator<String> iterator = set.iterator();
                for(iterator=set.iterator(); iterator.hasNext(); ){
                    String item = iterator.next();
                    if("4".equals(item)) {
                        try {
                            System.out.println("opopop");
                            Thread.sleep(5000);
                            iterator.remove();
                            System.out.println("opopop opopop");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
