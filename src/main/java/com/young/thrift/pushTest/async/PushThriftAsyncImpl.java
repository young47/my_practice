package com.young.thrift.pushTest.async;

import com.young.thrift.module.Push;
import com.young.thrift.module.Push1;
import com.young.thrift.module.Push2;
import com.young.thrift.pushTest.PushThrift;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PushThriftAsyncImpl implements PushThrift.AsyncIface {
    private static final AtomicInteger connections = new AtomicInteger(0);

    @Override
    public void push(long cid, int oid, AsyncMethodCallback<Boolean> resultHandler) throws TException {

    }

    @Override
    public void pushBatch(List<Long> cidList, int oid, AsyncMethodCallback<List<Long>> resultHandler) throws TException {

    }

    @Override
    public void ping(AsyncMethodCallback<Boolean> resultHandler) throws TException {

    }
}
