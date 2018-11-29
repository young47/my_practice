package com.young.thrift.pushTest.async;

import com.young.thrift.pushTest.PushThrift;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.apache.thrift.async.AsyncMethodCallback;

import java.util.concurrent.atomic.AtomicInteger;

public class PushCallback implements AsyncMethodCallback<Boolean> {
    private PushThrift.AsyncClient client;
    private Runnable runnable;
    public static final AtomicInteger pushed = new AtomicInteger(0);

    public PushCallback(PushThrift.AsyncClient client) {
        this.client = client;
    }

    public PushCallback(PushThrift.AsyncClient client, Runnable runnable) {
        this.client = client;
    }

    @Override
    public void onComplete(Boolean o) {
        System.out.println("第" + pushed.incrementAndGet() + "个 result:" + o+",currentThread:"+Thread.currentThread().getName());
    }

    @Override
    public void onError(Exception e) {
        System.out.println(e);
    }
}
