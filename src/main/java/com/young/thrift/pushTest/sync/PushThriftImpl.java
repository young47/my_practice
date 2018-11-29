package com.young.thrift.pushTest.sync;

import com.young.thrift.module.Push;
import com.young.thrift.pushTest.PushThrift;
import org.apache.thrift.TException;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PushThriftImpl implements PushThrift.Iface {
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
    public boolean pushList(List<Push> pushList) throws TException {
        for (Push push : pushList) {
            System.out.println(push.getUserInfo().getDt());
            connections.incrementAndGet();
        }
        System.out.println(Thread.currentThread().getName()+":"+connections.get());
        return false;
    }
}
