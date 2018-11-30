package com.young.thrift.pushTest.sync;

import com.young.thrift.ThriftConnectionPool;
import com.young.thrift.module.Push;
import com.young.thrift.module.UserInfo;
import com.young.thrift.pushTest.PushThrift;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PushClient {

    private static final int threads = 50;
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(threads);

    private static final AtomicInteger pushed = new AtomicInteger(0);
    private static final int max_connections = 1000;
    private static final ArrayList<PushThrift.Client> list = new ArrayList<>(max_connections);

    private static final ReentrantLock LOCK = new ReentrantLock(false);
    private static final Condition CONDITION = LOCK.newCondition();

    private static int available = 0, lent = 0;

    private static int num = 100000;
    private static int batch = 30;
    private static final ArrayBlockingQueue<Push> QUEUE = new ArrayBlockingQueue(num);



    public static void main(String[] args) throws TException, InterruptedException {
        producePush();
        /*CountDownLatch countDownLatch = new CountDownLatch(threads);
        long begin = System.currentTimeMillis();
        syncCall(countDownLatch);
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println(num + "个push cost：" + (end - begin) + "ms");*/

        CountDownLatch countDownLatch1 = new CountDownLatch(threads);
        long begin1 = System.currentTimeMillis();
        syncCallBatch(countDownLatch1);
        countDownLatch1.await();
        long end1 = System.currentTimeMillis();
        System.out.println(pushed.get() + "个push cost：" + (end1 - begin1) + "ms");

        EXECUTOR_SERVICE.shutdown();
    }

    private static void syncCallBatch(CountDownLatch countDownLatch) throws InterruptedException {
        for (int i = 0; i < threads; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ArrayList<Push> pushes = new ArrayList<>(batch);
                    int current = 0;
                    try {
                        Push push;
                        while ((push = QUEUE.poll()) != null) {
                            if (pushes.size() < batch) {
                                pushes.add(push);
                                if (pushes.size() == batch) {
                                    PushThrift.Client client = getClient();
                                    if (client != null) {
                                        client.pushList(pushes);
                                        pushed.addAndGet(pushes.size());
                                        current += pushes.size();
                                        pushes.clear();
                                        release(client);
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }
                        if (pushes.size() > 0) {
                            PushThrift.Client client = getClient();
                            if (client != null) {
                                client.pushList(pushes);
                                pushed.addAndGet(pushes.size());
                                pushes.clear();
                                release(client);
                            }
                        }

                    } catch (Exception e) {
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
            /*push.setMid("381815491");

            UserInfo userInfo = new UserInfo();
            userInfo.setImei("12367898765678");
            userInfo.setToken(1234325234);
            userInfo.setPt("723481df5c045473c5a24d2cdb5e8ec1fca2e0b1");
            userInfo.setDt("9e2d08a1e1bca8dabfcd391fed2b97e52ebaae952e5145a72c8515db12542d91");
            userInfo.setPm(i);
            userInfo.setTm(25479915);
            userInfo.setCh("1020");
            userInfo.setCid("6438994208421752841");


            push.setUserInfo(userInfo);*/
            QUEUE.put(push);
        }
        System.out.println("to push size=" + QUEUE.size());
    }

    private static void syncCall(CountDownLatch countDownLatch) {
        for (int i = 0; i < threads; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Push push;
                        while ((push = QUEUE.poll()) != null) {
                            Push finalPush = push;
                            //PushThrift.Client client = getClient();
                            PushThrift.Client client = ThriftConnectionPool.getClient();
                            if (client != null) {
                                boolean push1 = client.push(finalPush);
                                //System.out.println("返回：" + push1 + ",pushed=" + pushed.incrementAndGet());
                                release(client);
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

    private static PushThrift.Client getClient() throws TTransportException {
        try {
            for (; ; ) {
                LOCK.lock();
                available = list.size();
                if (available > 0) {
                    return list.remove(available - 1);
                } else if ((lent + available) < max_connections) {
                    TTransport transport = new TFastFramedTransport(new TSocket("10.2.131.165", PushServer.port));
                    //TTransport transport = new TSocket("127.0.0.1", PushServer.port);
                    TProtocol protocol = new TCompactProtocol(transport);
                    PushThrift.Client client = new PushThrift.Client(protocol);
                    transport.open();
                    //System.out.println("sockets=" + sockets.incrementAndGet());
                    //list.add(client);
                    lent++;
                    return client;
                } else {
                    CONDITION.await();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            LOCK.unlock();
        }
    }

    private static void release(PushThrift.Client client) {
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
