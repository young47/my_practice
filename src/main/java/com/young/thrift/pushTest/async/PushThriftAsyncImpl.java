package com.young.thrift.pushTest.async;

import com.young.thrift.module.Push;
import com.young.thrift.pushTest.PushThrift;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PushThriftAsyncImpl implements PushThrift.AsyncIface {
    private static final AtomicInteger connections = new AtomicInteger(0);

    @Override
    public void push(Push push, AsyncMethodCallback<Boolean> resultHandler) throws TException {
        push.setMid("1234");
        System.out.println(Thread.currentThread().getName()+":"+connections.incrementAndGet());
        resultHandler.onComplete(false);
    }

    @Override
    public void contains(String cid, String oid, AsyncMethodCallback<Boolean> resultHandler) throws TException {

    }

    @Override
    public void pushList(List<Push> pushList, AsyncMethodCallback<Boolean> resultHandler) throws TException {

    }
}
