package com.young.leveldb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class LevelDbTest {

    private static final int keys = 2000_0000;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LevelDb levelDb = new LevelDb(args[1]+"/leveldb");

        String init = args[0];
        if (Integer.valueOf(init)==1){
            init(levelDb);
        }
        ConcurrentHashMap<String, Long> map = iter(levelDb);
        test(map);
        executorService.shutdownNow();
    }

    private static void test(ConcurrentHashMap<String, Long> map) {
        System.out.println(map.size());
        int error = 0;
        for (int i = 0; i < keys; i++) {
            if (!map.containsKey(6438994208421752841l + i+"")) {
                error++;
            }
        }
        System.out.println("error=" + error);
    }

    private static ConcurrentHashMap<String, Long> iter(LevelDb levelDb) throws ExecutionException, InterruptedException {
        Iterator<Map.Entry<byte[], byte[]>> iterator = levelDb.getIterator();
        int keyNum = 0;
        ConcurrentHashMap<String, Long> concurrentHashMap = new ConcurrentHashMap<>(200000);
        List<Future<String>> futureList = new ArrayList<Future<String>>();
        long begin = System.currentTimeMillis();
        while (iterator.hasNext()) {
            Map.Entry<byte[], byte[]> item = iterator.next();
            keyNum++;
            Future<String> future = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    byte[] key = item.getKey();
                    byte[] value = item.getValue();
                    concurrentHashMap.put(makeLong(key) + "", makeLong(value));
                    return "success";
                }
            });
            futureList.add(future);
            if (keyNum%1000000==0){
                System.out.println("map size="+concurrentHashMap.size());
            }
        }

        for (Future<String> future : futureList) {
            future.get();
        }
        //任务循环完一次需要关闭db
        levelDb.closeIterator();
        long end = System.currentTimeMillis();
        System.out.println("iter cost:" + (end - begin) + "ms");
        return concurrentHashMap;
    }

    private static void init(LevelDb levelDb) {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < keys; i++) {
            long cid = 6438994208421752841l + i;
            long now = System.currentTimeMillis();
            levelDb.put(longToBytes(cid), longToBytes(now));
        }
        long end = System.currentTimeMillis();
        System.out.println("init cost:" + (end - begin) + "ms");
    }

    private static byte[] longToBytes(long x) {
        byte[] bytes = new byte[Long.BYTES];
        for (int i = 0; i < bytes.length; i++) {
            int shift = (bytes.length - 1 - i) * 8;
            //System.out.println("shift=" + shift);
            bytes[i] = (byte) (x >> shift);
        }
        return bytes;
    }

    static private long makeLong(byte[] bytes) {
        return ((((long) bytes[0]) << 56) |
                (((long) bytes[1] & 0xff) << 48) |
                (((long) bytes[2] & 0xff) << 40) |
                (((long) bytes[3] & 0xff) << 32) |
                (((long) bytes[4] & 0xff) << 24) |
                (((long) bytes[5] & 0xff) << 16) |
                (((long) bytes[6] & 0xff) << 8) |
                (((long) bytes[7] & 0xff)));
    }
}
