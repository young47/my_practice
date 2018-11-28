package com.young.thrift.pushTest;

import com.young.thrift.WorkerThrift;
import com.young.thrift.module.Push;
import com.young.thrift.module.UserInfo;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class PushClient {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(50);

    private static final AtomicInteger sockets = new AtomicInteger(0);

    private static int num = 10000;
    private static final BlockingQueue<Push> QUEUE = new ArrayBlockingQueue(num);
    public static void main(String[] args) throws TException, InterruptedException {
        producePush();
        long begin = System.currentTimeMillis();
        syncCall();
        long end = System.currentTimeMillis();
        System.out.println(num+"个push cost："+(end-begin)+"ms");
    }

    private static void producePush() throws InterruptedException {
        for (int i = 0; i < num; i++) {
            Push push = new Push();
            push.setCid("6438994208421752841");
            push.setOid("324058455");
            push.setMid("381815491");

            UserInfo userInfo = new UserInfo();
            userInfo.setImei("123");
            userInfo.setToken(1234325234);
            userInfo.setPt("ABC");

            push.setUserInfo(userInfo);
            QUEUE.put(push);
        }
    }

    private static void syncCall() {
        Push push;
        while ((push = QUEUE.poll())!=null) {
            Push finalPush = push;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        //System.out.println(finalPush.getMid());
                        getClient().push(finalPush);
                    } catch (TException e) {
                        e.printStackTrace();
                    }
                }
            };
            EXECUTOR_SERVICE.execute(runnable);
        }
    }

    private static PushThrift.Client getClient() throws TTransportException {
        TTransport transport = new TFastFramedTransport(new TSocket("127.0.0.1", PushServer.port));
        TProtocol protocol = new TCompactProtocol(transport);
        PushThrift.Client client = new PushThrift.Client(protocol);
        transport.open();
        System.out.println("sockets="+sockets.incrementAndGet());
        return client;
    }
}
