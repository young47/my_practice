package com.young.thread.pool;

import com.google.common.base.Splitter;
import com.young.lamda.ParallelTest;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

public class ThreadPoolTest {

    private static File bF = new File("/Users/young/Desktop/temp/loc_440000.cidlist");
    private static File hF = new File("/Users/young/Desktop/temp/sub_229.cidlist");
    private static final ExecutorService pool = Executors.newWorkStealingPool(10);

    public static void main(String[] args) throws Exception{
        Set<String> users = ParallelTest.readFile(bF);
        users.addAll(ParallelTest.readFile(hF));
        System.out.println("users.size()=" + users.size());

        List<Set<String>> sets = splitUsers(users);
        int i = 0;
        for (Set<String> set : sets) {
            System.out.println("set "+(i++)+" size="+set.size());
            pool.execute(new Task(set));
        }
        pool.shutdown();
        while (!pool.awaitTermination(2, TimeUnit.SECONDS)) {
            System.out.println("Pool statistics : "+pool.toString());
        }
    }


    private static List<Set<String>> splitUsers(Set<String> receivers) {
        List<Set<String>> userSetBuckets = new ArrayList<Set<String>>();
        for (int i = 0; i < 10; ++i) {
            userSetBuckets.add(new HashSet<String>());
        }
        receivers.parallelStream().forEach(cid -> {
            int idx = Math.abs(cid.hashCode()) % 10;
            userSetBuckets.get(idx).add(cid);
        });
        return userSetBuckets;
    }

    static class Task implements Runnable{
        private Set<String> users;

        public Task(Set<String> users) {
            this.users = users;
        }

        @Override
        public void run() {
            int i = 0;
            while (i<=users.size()) {
                users.stream().parallel().forEach(u->{
                    Math.abs(u.hashCode());
                });
                i++;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
