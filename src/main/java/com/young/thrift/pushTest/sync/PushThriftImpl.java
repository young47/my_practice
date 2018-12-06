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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PushThriftImpl implements PushThrift.Iface {
    private static final File DUMP = new File("/Users/young/Desktop/temp/bloom-filter-test.dump");
    private static PushBloomFilter BLOOM_FILTER;
    private static final int num = 1000_0000;
    private static Map<String, Long> updateTime = new ConcurrentHashMap<>(num);
    static {
        initBloom();
    }

    @Override
    public boolean push1(Push1 push) throws TException {
        return false;
    }

    @Override
    public boolean push2(Push2 push) throws TException {
        return false;
    }

    private static void initBloom() {
        BLOOM_FILTER = PushBloomFilter.create(BloomFilterParams.MAX_KEY_INSERTED, 0.0001d, DUMP);
        try {
            PushBloomFilterTest.fillingFilter(BLOOM_FILTER);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("begin to init cid map");
        long cid = 6438994208421752841l;
        for (int i = 0; i < num; i++) {
            updateTime.put(cid+i +"", 100000l);
        }
        System.out.println("end to init cid map");

    }

    @Override
    public List<String> checkAndPutIfAbsent(List<String> cids) throws TException {
        for (String cid : cids) {
            BLOOM_FILTER.put(cid);
            Long aLong = updateTime.get(cid);
        }
        System.out.println(Thread.currentThread().getName()+":"+cids.size());
        return cids;
    }

    private static final AtomicInteger connections = new AtomicInteger(0);

    @Override
    public boolean push(Push push) throws TException {
        push.setMid("1234");
        System.out.println(Thread.currentThread().getName()+":"+connections.incrementAndGet());
        return true;
    }

    @Override
    public boolean contains(String cid, String oid) throws TException {
        cid += oid;
        return true;
    }

    @Override
    public boolean ping() throws TException {
        return true;
    }

    @Override
    public boolean pushLong(long cid, int oid) throws TException {
        return false;
    }

    @Override
    public boolean pushList(List<Push> pushList) throws TException {
        for (Push push : pushList) {
            System.out.println(push.getUserInfo().getDt());
            connections.incrementAndGet();
        }
        System.out.println(Thread.currentThread().getName()+":"+connections.get());
        return false;
    }
}
