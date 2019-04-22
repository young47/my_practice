package com.young.thrift.pushTest.sync;

import com.young.temp.BloomFilterParams;
import com.young.temp.PushBloomFilter;
import com.young.temp.PushBloomFilterTest;
import com.young.thrift.module.Push;
import com.young.thrift.module.Push1;
import com.young.thrift.module.Push2;
import com.young.thrift.pushTest.PushThrift;
import org.apache.thrift.TException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PushThriftImpl implements PushThrift.Iface {
    private static final File DUMP = new File("/Users/young/Desktop/temp/bloom-filter-test.dump");
    private static PushBloomFilter BLOOM_FILTER;
    private static final int num = 1000_0000;
    private static Map<String, Long> updateTime = new ConcurrentHashMap<>(num);
    private static AtomicInteger received = new AtomicInteger(0);
    private static final AtomicInteger connections = new AtomicInteger(0);
    private static String _1M = "";
    static {
        //initBloom();
       /* StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 10_0000; i++) {
            builder.append("0123456789");
        }
        _1M = builder.toString();*/
    }

    @Override
    public boolean push(long cid, int oid) throws TException {
        return false;
    }

    @Override
    public List<Long> pushBatch(List<Long> cidList, int oid) throws TException {
        ArrayList<Long> longs = new ArrayList<>(cidList.size());
        for (Long aLong : cidList) {
           /* for (int i = 0; i < 16; i++) {
                aLong.hashCode();
            }*/
            longs.add(aLong);
        }
        return longs;
    }

    private static void initBloom() {
        BLOOM_FILTER = PushBloomFilter.create(BloomFilterParams.MAX_KEY_INSERTED, 0.0001d, DUMP);
        /*try {
            PushBloomFilterTest.fillingFilter(BLOOM_FILTER);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
       /* System.out.println("begin to init cid map");
        long cid = 6438994208421752841l;
        for (int i = 0; i < num; i++) {
            updateTime.put(cid+i +"", 100000l);
        }
        System.out.println("end to init cid map");*/

    }

    public List<String> checkAndPutIfAbsent(List<String> cids) throws TException {
        for (String cid : cids) {
            if (BLOOM_FILTER.put(cid)){
                received.incrementAndGet();

            }else {
                System.out.println("cid="+cid+" exists!");
            }
            //Long aLong = updateTime.get(cid);
           /* cid.hashCode();
            cid.hashCode();

            cid.hashCode();

            cid.hashCode();

            cid.hashCode();

            cid.hashCode();*/
        }
        System.out.println(Thread.currentThread().getName()+":"+received.get());
        return cids;
    }

    @Override
    public boolean ping() throws TException {
        return false;
    }
}
