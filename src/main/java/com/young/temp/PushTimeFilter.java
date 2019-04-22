package com.young.temp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static com.young.temp.PushTimeFilterParams.KEYS_PER_MAP;
import static com.young.temp.PushTimeFilterParams.KV_BYTES;
import static com.young.temp.PushTimeFilterParams.K_BYTES;

public class PushTimeFilter {
    private static final Logger logger = LoggerFactory.getLogger(PushTimeFilter.class);

    private static PushTimeFilter INSTANCE = new PushTimeFilter();
    private static Map<String, Long> pushTimeMap;
    private static PushTime[] pushTimeList;
    private static PushTime newCidPushTime;

    public static PushTimeFilter init() throws Exception {
        initMapFrom(PushTimeFilterParams.DUMP_FILE);
        //startDumpThread();
        //startClearThread();
        return INSTANCE;
    }

    private static void initMapFrom(String dump) throws IOException {
        try {
            File mapFile = new File(dump);
            RandomAccessFile file = new RandomAccessFile(mapFile, "rw");
            int totalKeys = (int) (file.getChannel().size() / KV_BYTES);
            int size = totalKeys / KEYS_PER_MAP + 1;
            pushTimeList = new PushTime[size];
            CountDownLatch countDownLatch = new CountDownLatch(size);
            long begin = System.currentTimeMillis();
            Task task = null;
            for (int i = 0; i < size; i++) {
                if (i == size - 1) {
                    task = new Task(i, countDownLatch, mapFile, i * KEYS_PER_MAP, totalKeys - i * KEYS_PER_MAP);
                } else {
                    task = new Task(i, countDownLatch, mapFile, i * KEYS_PER_MAP, KEYS_PER_MAP);
                }
                new Thread(task).start();
            }
            countDownLatch.await();
            long end = System.currentTimeMillis();
            logger.info("Init {} pushTimeMap from mapFile costs : {}ms", size, (end - begin));

            //创建一个空的Map来保存新的cid
            newCidPushTime = new PushTime(mapFile, pushTimeList[pushTimeList.length - 1].end, KEYS_PER_MAP >> 1);
            logger.info("create empty PushTimeMap for new cid");
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e; //io错误会导致初始化失败，抛出去
        }

    }

    /**
     * 删掉过期的数据
     */
   /* private static void startClearThread() {
        Thread pseudoClearThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (String cid : pushTimeMap.keySet()) {
                        if (removeIfNeeded(cid)) {
                            //再次进行判断
                            if (pushTimeMap.get(cid) == PushTimeFilterParams.KEY_REMOVED_FLAG) {
                                logger.warn("cid={} pushTime is deleted!", cid);
                                //sendKafka(cid);
                            }
                        }
                    }
                }
            }
        });
        pseudoClearThread.setDaemon(true);
        pseudoClearThread.start();
        logger.info("pseudoClear-pushTimeMap-Thread started successfully");

        *//*Thread realClearThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //每天凌晨1点到5点时清除map
                    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    if (hour >= 1 && hour <= 5) {
                        if (pushTimeMap.capacity() > 0 && GlobalParams.THREAD_POOL_EXECUTOR.getActiveCount() == 0) {
                            for (String cid : pushTimeMap.keySet()) {
                                if (pushTimeMap.get(cid) == PushTimeFilterParams.KEY_REMOVED_FLAG) {
                                    pushTimeMap.remove(cid);
                                }
                            }
                        }
                    }
                    try {
                        Thread.sleep(60 * 60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        realClearThread.setDaemon(true);
        realClearThread.start();
        logger.info("realClear-pushTimeMap-Thread started successfully");*//*
    }*/

    /**
     * dump pushTimeMap到本地硬盘
     */
   /* private static void startDumpThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ObjectOutputStream outputStream;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    try {
                        if (PushTimeFilterParams.GZIP_COMPRESS) {
                            byteArrayOutputStream = new ByteArrayOutputStream();
                            outputStream = new ObjectOutputStream(byteArrayOutputStream);
                            outputStream.writeObject(pushTimeMap);
                            byte[] compressed = GzipUtil.compress(byteArrayOutputStream.toByteArray());
                            ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream(compressed.length);
                            byteArrayOutputStream1.write(compressed, 0, compressed.length);
                            byteArrayOutputStream1.writeTo(new FileOutputStream(PushTimeFilterParams
                                    .DUMP_FILE));
                        } else {
                            outputStream = new ObjectOutputStream(new FileOutputStream(PushTimeFilterParams
                                    .DUMP_FILE));
                            outputStream.writeObject(pushTimeMap);
                            Thread.sleep(PushTimeFilterParams.DUMP_PERIOD_IN_MILLS);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        logger.info("dump-pushTimeMap-thread started successfully");
    }*/
    public Long get(Long cid) {
        for (int i = 0; i < pushTimeList.length; i++) {
            if (pushTimeList[i].contain(cid)) {
                return pushTimeList[i].get(cid);
            }
        }
        return null;
    }

    public boolean put(Long cid) {
        PushTime pushTime = pushTimeOf(cid);
        if (pushTime == null) {//新的cid
            return newCidPushTime.put(cid);
        } else {
            return pushTime.put(cid);
        }
    }

    private PushTime pushTimeOf(Long cid) {
        for (int i = 0; i < pushTimeList.length; i++) {
            if (pushTimeList[i].contain(cid)) {
                return pushTimeList[i];
            }
        }
        return null;
    }

    /**
     * 为了便于处理，这里没有真正的删除，而只是做了标记；等系统空闲时，再进行真正地删除
     *
     * @param cid
     * @return
     */
    private static boolean removeIfNeeded(String cid) {
        Long origin = pushTimeMap.get(cid);
        if (origin == PushTimeFilterParams.KEY_REMOVED_FLAG || origin == null) {
            return false;
        } else if (System.currentTimeMillis() - origin >= PushTimeFilterParams.PUSH_TIME_VALID_SPAN_IN_MILLS) {
            return pushTimeMap.replace(cid, origin, PushTimeFilterParams.KEY_REMOVED_FLAG);
        }
        return false;
    }


    private static class PushTime {
        private Map<Long, Long> pushTimeMap = new ConcurrentHashMap<>();
        /**
         * key begin index
         */
        private int begin;
        /**
         * key end index
         */
        private int end;
        private int capacity;
        private File mapFile;
        private FileChannel fileChannel;
        private MappedByteBuffer mbb;

        private PushTime(File mapFile, int begin, int capacity) throws IOException {
            this.pushTimeMap = new ConcurrentHashMap<>(capacity * 4 / 3);
            this.mapFile = mapFile;
            this.begin = begin;
            this.end = capacity + begin;
            this.capacity = capacity;
            this.fileChannel = new RandomAccessFile(mapFile, "rw").getChannel();
            this.mbb = fileChannel.map(FileChannel.MapMode.READ_WRITE, begin, capacity);
            initMap();
        }

        private void initMap() {
            for (int i = 0; i < this.capacity; i++) {
                int keyIndex = i * KV_BYTES;
                int valueIndex = keyIndex + K_BYTES;
                long cid = mbb.getLong(keyIndex);
                if (cid != 0) {
                    pushTimeMap.put(cid, mbb.getLong(valueIndex));
                } else {//说明到最后了
                    break;
                }
            }
        }

        private boolean contain(Long cid) {
            return pushTimeMap.containsKey(cid);
        }

        /**
         * 对同一个cid，ConcurrentHashMap可能有如下三种并发修改顺序：
         * <p>
         * 1. 没有remove， put->put->put...
         * 2. remove在前， remove->put->put...
         * 3. remove在中间，put->put->remove->put->put
         * <p>
         *
         * @param cid
         * @return
         */
        private boolean put(Long cid) {
            Long origin = pushTimeMap.get(cid);
            long now = System.currentTimeMillis();
            if (origin == null || origin == PushTimeFilterParams.KEY_REMOVED_FLAG) {
                Long pushTime = pushTimeMap.put(cid, now);
                return pushTime == origin;
            } else if ((now - origin >= PushTimeFilterParams.PUSH_TIME_VALID_SPAN_IN_MILLS)) {
                boolean replace = pushTimeMap.replace(cid, origin, now);
                //如果是第二种情况的话，replace会失败
                Long realValue = pushTimeMap.get(cid);
                if (!replace && realValue == PushTimeFilterParams.KEY_REMOVED_FLAG) {
                    //第二种情况再试一次，否则put都会失败
                    return put(cid);
                }
                return replace;
            }
            return false;
        }

        private Long get(Long cid) {
            return pushTimeMap.get(cid);
        }
    }

    private static class Task implements Runnable {
        private CountDownLatch countDownLatch;
        private File mapFile;
        private int begin;
        private int size;
        private int index;

        private Task(int index, CountDownLatch countDownLatch, File mapFile, int begin, int size) {
            this.countDownLatch = countDownLatch;
            this.mapFile = mapFile;
            this.begin = begin;
            this.size = size;
            this.index = index;
        }

        @Override
        public void run() {
            try {
                pushTimeList[index] = new PushTime(mapFile, begin, size);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
