package com.young.temp;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class PushTimeFilter {
    private static int cidNums = 2000_0000;
    private static Map<String, Long> pushTimeMap = new ConcurrentHashMap<>(20000);
    private static Map<Long, Integer> indexMap = new ConcurrentHashMap<>(20000);
    private static Bits bits = new Bits();

    private static final int KVByteSize = Long.BYTES + Long.BYTES;

    public static void main(String[] args) throws InterruptedException {
        File file = new File(args[0] + "/dump/pushTime.dump");
        int fp = Integer.valueOf(args[1]);
        if (fp == 1) {
            init();
            pushTimeMap.clear();
        }
        int threads = args.length > 2 ? Integer.valueOf(args[2]) : Runtime.getRuntime().availableProcessors();

        cidNums = Integer.valueOf(args[3]);
        bits.allocate(file, cidNums);

        /*initFromFile(threads);
        testMap();*/
        initFromFile2(threads);
    }

    private static void initFromFile2(int mapNums) throws InterruptedException {
        if (mapNums <= 0) {
            return;
        }
        long begin = System.currentTimeMillis();
        List<Map<String, Long>> mapList = new ArrayList<Map<String, Long>>(mapNums);
        for (int i = 0; i < mapNums; i++) {
            int mapSize = cidNums / mapNums;
            Map<String, Long> concurrentHashMap = new ConcurrentHashMap<>(mapSize);
            mapList.add(concurrentHashMap);
        }
        System.out.println("mapList size=" + mapList.size());
        bits.initMap2(mapList);
        long end = System.currentTimeMillis();
        System.out.println("initMap " + cidNums + " cost:" + (end - begin) + "ms");
        for (Map<String, Long> map : mapList) {
            System.out.println("map size=" + map.size());
        }
    }

    private static void initFromFile(int threads) throws InterruptedException {
        long begin = System.currentTimeMillis();
        bits.initMap(threads);
        long end = System.currentTimeMillis();
        System.out.println("initMap " + cidNums + " cost:" + (end - begin) + "ms");
        System.out.println("PushTimeMap size="+pushTimeMap.size());
    }

    private static void testMap() {
        int error = 0;
        for (int i = 0; i < cidNums; i++) {
            long key = 6438994208421752841l + i;
            if (!pushTimeMap.containsKey(key + "")) {
                error++;
            }
        }
        System.out.println("test finished! error=" + error);
        System.out.println("indexMap=" + indexMap.size());
        /*for (String cid : pushTimeMap.keySet()) {
            System.out.println("cid=" + cid);
        }*/
    }

    public static void init() {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < cidNums; i++) {
            put(6438994208421752841l + i);
            //pushTimeMap.put(6438994208421752841l + i + "", System.currentTimeMillis());
        }
        long end = System.currentTimeMillis();
        System.out.println("put " + cidNums + " cost:" + (end - begin) + "ms");
    }


    private static class Bits {
        private File DUMP_FILE;
        private MappedByteBuffer mbb;

        private boolean allocate(File dumpFile, int keyNums) {
            long begin = System.currentTimeMillis();
            this.DUMP_FILE = dumpFile;
            try {
                if (!dumpFile.exists()) {
                    dumpFile.createNewFile();
                }
                final RandomAccessFile file = new RandomAccessFile(dumpFile, "rw");
                //boolean initNeeded = ((byte) file.read()) != MAGIC;
                //int byteNumContainingMetaInfo = byteNums + META_NUM;
                /*if (byteNumContainingMetaInfo > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("Params is invalid! Maybe keyCapacity is too large or fpp is too small!");
                }*/
                this.mbb = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, KVByteSize * keyNums);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            long end = System.currentTimeMillis();
            System.out.println("allocate cost:" + (end - begin) + "ms");
            return true;
        }

        private void initMap(int threads) throws InterruptedException {
            int cidNumPerThread = cidNums / threads;
            CountDownLatch countDownLatch = new CountDownLatch(threads);
            System.out.println("threads=" + threads);
            MyTask myTask;
            for (int i = 0; i < threads; i++) {
                if (i == threads - 1) {
                    myTask = new MyTask(countDownLatch, i * cidNumPerThread, cidNums - 1);
                } else {
                    myTask = new MyTask(countDownLatch, i * cidNumPerThread, (i + 1) * cidNumPerThread);
                }
                new Thread(myTask).start();
            }
            countDownLatch.await();
        }

        private boolean set(long cid, long pushTime) {
            mbb.putLong(cid).putLong(pushTime);
            return true;
        }

        public void initMap2(List<Map<String, Long>> mapList) throws InterruptedException {
            int threads = mapList.size();
            int cidNumPerThread = cidNums / threads + 1;
            CountDownLatch countDownLatch = new CountDownLatch(threads);
            MyTask2 myTask;
            for (int i = 0; i < threads; i++) {
                if (i == threads - 1) {
                    myTask = new MyTask2(countDownLatch, i * cidNumPerThread, cidNums - 1, mapList.get(i));
                } else {
                    myTask = new MyTask2(countDownLatch, i * cidNumPerThread, (i + 1) * cidNumPerThread, mapList.get(i));
                }
                new Thread(myTask).start();
            }
            countDownLatch.await();
        }

        private class MyTask implements Runnable {
            private CountDownLatch countDownLatch;
            private int begin;
            private int end;

            private static final int step = 16;

            public MyTask(CountDownLatch countDownLatch, int begin, int end) {
                this.countDownLatch = countDownLatch;
                this.begin = begin;
                this.end = end;
            }

            @Override
            public void run() {
                for (int j = begin; j < end; j++) {
                    int keyIndex = j * step;
                    int valueIndex = keyIndex + 8;
                    //long cid = mbb.getLong(keyIndex);
                    long value = mbb.getLong(valueIndex);
                    /*if (value == 0) {
                        continue;
                    }*/
                    pushTimeMap.put(mbb.getLong(keyIndex) + "", value);
                    indexMap.put(mbb.getLong(keyIndex), keyIndex);
                }
                System.out.println(Thread.currentThread().getName() + " finished, begin=" + begin + ",end=" + end);
                countDownLatch.countDown();
            }
        }

        private class MyTask2 implements Runnable {
            private CountDownLatch countDownLatch;
            private int begin;
            private int end;
            private Map map;

            private static final int step = 16;

            public MyTask2(CountDownLatch countDownLatch, int begin, int end, Map<String, Long> map) {
                this.countDownLatch = countDownLatch;
                this.begin = begin;
                this.end = end;
                this.map = map;
            }

            @Override
            public void run() {
                for (int j = begin; j < end; j++) {
                    int keyIndex = j * step;
                    int valueIndex = keyIndex + 8;
                    //long cid = mbb.getLong(keyIndex);
                    long value = mbb.getLong(valueIndex);
                    /*if (value == 0) {
                        continue;
                    }*/
                    map.put(mbb.getLong(keyIndex) + "", value);
                }
                System.out.println(Thread.currentThread().getName() + " finished, begin=" + begin + ",end=" + end);
                countDownLatch.countDown();
            }
        }
    }

    public static void put(Long cid) {
        long value = 0;
        /*if (cid % 10 == 0) {
            value = 0;
        } else {
            value = System.currentTimeMillis();
        }*/
        value = System.currentTimeMillis();

        pushTimeMap.put(cid.toString(), value);

        bits.set(cid, value);
    }

    public static long get(Long cid) {
        return pushTimeMap.get(cid);
    }

}
