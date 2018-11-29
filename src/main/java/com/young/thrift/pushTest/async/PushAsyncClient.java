package com.young.thrift.pushTest.async;

import com.young.thrift.module.Push;
import com.young.thrift.module.UserInfo;
import com.young.thrift.pushTest.PushThrift;
import com.young.thrift.pushTest.sync.PushServer;
import org.apache.thrift.TException;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PushAsyncClient {

    private static final int threads = 10;
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(threads);

    private static final int max_connections = 1000;
    private static final ArrayList<PushThrift.AsyncClient> list = new ArrayList<>(max_connections);

    private static final ReentrantLock LOCK = new ReentrantLock(false);
    private static final Condition CONDITION = LOCK.newCondition();

    private static int available = 0, lent = 0;

    private static int num = 500;
    private static final BlockingQueue<Push> QUEUE = new ArrayBlockingQueue(num);

    private static  TAsyncClientManager clientManager;
    static {
        try {
            clientManager = new TAsyncClientManager();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws TException, InterruptedException {
        producePush();
        CountDownLatch countDownLatch = new CountDownLatch(threads);
        long begin = System.currentTimeMillis();
        asyncCall(countDownLatch);
        /*PushThrift.AsyncClient client = getClient();
        if (client != null) {
            System.out.println("push thread:"+Thread.currentThread().getName());
            client.push(QUEUE.take(), new PushCallback(client));
            release(client);
        }*/
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println(num + "个push cost：" + (end - begin) + "ms");
    }

    private static void asyncCall(CountDownLatch countDownLatch) {
        for (int i = 0; i < threads; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Push push;
                        while ((push = QUEUE.poll()) != null) {
                            Push finalPush = push;
                            PushThrift.AsyncClient client = getClient();
                            if (client != null) {
                                client.push(finalPush, new PushCallback(client));
                                //release(client);
                            }
                        }
                    } catch (TException e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            };
            EXECUTOR_SERVICE.execute(runnable);
        }
    }

    private static void producePush() throws InterruptedException {
        for (int i = 0; i < num; i++) {
            Push push = new Push();
            push.setCid("6438994208421752841");
            push.setOid("324058455");
            push.setMid("381815491");

            UserInfo userInfo = new UserInfo();
            userInfo.setImei("12367898765678");
            userInfo.setToken(1234325234);
            userInfo.setPt("723481df5c045473c5a24d2cdb5e8ec1fca2e0b1");
            userInfo.setDt("9e2d08a1e1bca8dabfcd391fed2b97e52ebaae952e5145a72c8515db12542d91");
            userInfo.setPm(1);
            userInfo.setTm(25479915);
            userInfo.setCh("1020");
            userInfo.setCid("6438994208421752841");


            push.setUserInfo(userInfo);
            QUEUE.put(push);
        }
        System.out.println("to push size=" + QUEUE.size());
    }


    private static PushThrift.AsyncClient getClient() throws TTransportException {
        try {
            for (; ; ) {
                LOCK.lock();
                available = list.size();
                if (available > 0) {
                    return list.remove(available - 1);
                } else if ((lent + available) < max_connections) {
                    TNonblockingTransport transport = new TNonblockingSocket("127.0.0.1", PushServer.port);
                    TCompactProtocol.Factory protocolFactory = new TCompactProtocol.Factory();
                    //PushThrift.AsyncClient client = new PushThrift.AsyncClient(protocolFactory, clientManager, transport);
                    PushThrift.AsyncClient.Factory clientFactory = new PushThrift.AsyncClient.Factory(clientManager, protocolFactory);

                    PushThrift.AsyncClient asyncClient = clientFactory.getAsyncClient(transport);
                    //transport.open();
                    //System.out.println("sockets=" + sockets.incrementAndGet());
                    //list.add(client);
                    lent++;
                    return asyncClient;
                } else {
                    CONDITION.await();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            LOCK.unlock();
        }
    }

    public static void release(PushThrift.AsyncClient client) {
        try {
            LOCK.lock();
            list.add(client);
            available++;
            lent--;
            CONDITION.signalAll();
        } finally {
            LOCK.unlock();
        }
    }
}
