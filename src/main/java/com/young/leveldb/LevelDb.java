package com.young.leveldb;

import org.iq80.leveldb.*;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by jamesou on 2018/4/12.
 */
public class LevelDb implements Db<byte[], byte[]> {

    private DB db;

    private DBIterator iterator;
    private Snapshot snapshot;

    public LevelDb(String path) {
        try {
            DBComparator comparator = new DBComparator() {
                @Override
                public int compare(byte[] key1, byte[] key2) {

                    return ByteBuffer.wrap(key1).compareTo(ByteBuffer.wrap(key2));
                }

                @Override
                public String name() {
                    return "simple";
                }

                @Override
                public byte[] findShortestSeparator(byte[] start, byte[] limit) {
                    return start;
                }

                @Override
                public byte[] findShortSuccessor(byte[] key) {
                    return key;
                }

            };

            // init
            // Iq80DBFactory.factory;
            DBFactory factory = org.fusesource.leveldbjni.JniDBFactory.factory;
            File dir = new File(path);
            Options options = new Options().createIfMissing(true);
            options.blockSize(4096 * 10).cacheSize(4096 * 10);
            System.out.println("blockSize="+options.blockSize());
            System.out.println("CacheSize="+options.cacheSize());
            options.comparator(comparator);
            // 重新open新的db
            db = factory.open(dir, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Iterator<Map.Entry<byte[], byte[]>> getIterator() {
        snapshot = db.getSnapshot();
        // 读选项
        ReadOptions readOptions = new ReadOptions();
        // 遍历中swap出来的数据，不应该保存在memtable中。
        readOptions.fillCache(false);
        // 默认snapshot为当前。
        readOptions.snapshot(snapshot);
        iterator = db.iterator(readOptions);

        return iterator;
    }

    @Override
    public void closeIterator() {
        try {
            iterator.close();
            snapshot.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] get(byte[] bytes) {
        return db.get(bytes);
    }

    @Override
    public void put(byte[] bytes, byte[] k1) {
        db.put(bytes, k1);
    }

    @Override
    public void delete(byte[] bytes) {
        db.delete(bytes);
    }
}
