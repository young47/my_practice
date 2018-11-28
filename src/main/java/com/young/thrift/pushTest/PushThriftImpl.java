package com.young.thrift.pushTest;

import com.young.thrift.module.Push;
import org.apache.thrift.TException;

public class PushThriftImpl implements PushThrift.Iface {
    @Override
    public boolean push(Push push) throws TException {
        push.setMid("1234");
        System.out.println(Thread.currentThread().getName()+":"+push.getMid());
        return true;
    }

    @Override
    public boolean contains(String cid, String oid) throws TException {
        cid += oid;
        return true;
    }
}
