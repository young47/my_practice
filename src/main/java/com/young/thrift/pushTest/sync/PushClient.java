package com.young.thrift.pushTest.sync;

import com.young.thrift.ThriftConnectionPool;
import com.young.thrift.ThriftPooledConnection;
import com.young.thrift.module.Push;
import com.young.thrift.module.Push1;
import com.young.thrift.module.Push2;
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
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static io.netty.util.ReferenceCountUtil.release;

public class PushClient {

    private static final int threads = 1;
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(threads+1);

    private static final AtomicInteger pushed = new AtomicInteger(0);
    private static final int max_connections = 1000;
    private static final ArrayList<PushThrift.Client> list = new ArrayList<>(max_connections);

    private static final ReentrantLock LOCK = new ReentrantLock(false);
    private static final Condition CONDITION = LOCK.newCondition();

    private static int available = 0, lent = 0;

    private static int num = 1;
    private static int batch = 5000;
    private static final ArrayBlockingQueue<Push> QUEUE = new ArrayBlockingQueue(num);
    private static final ArrayBlockingQueue<String> QUEUE2 = new ArrayBlockingQueue(num);


    public static void main(String[] args) throws TException, InterruptedException {
        //producePush();
        //test();
        //test_2();
        test_3();

        //testBatch_1();
        //testBatch_2();

        EXECUTOR_SERVICE.shutdown();
    }

    private static void test_3() throws InterruptedException {
        final ThriftPooledConnection connection = ThriftConnectionPool.getConnection();
        PushThrift.Client client = connection.getClient();
        long cid = 888888888888888l;
        int oid = 55555555;
        new Thread(new Runnable() {
            @Override
            public void run() {
                long begin = System.currentTimeMillis();
                try {
                    boolean result = client.pushLong(cid, oid);
                } catch (TException e) {
                    e.printStackTrace();
                } finally {
                    ThriftConnectionPool.release(connection);
                }
                long end = System.currentTimeMillis();
                System.out.println("1个push long cost：" + (end - begin) + "ms");
            }
        }).start();
        final ThriftPooledConnection connection1 = ThriftConnectionPool.getConnection();
        PushThrift.Client client1 = connection1.getClient();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long begin = System.currentTimeMillis();
                try {
                    boolean result = client1.push2(new Push2(cid+"",oid+""));
                } catch (TException e) {
                    e.printStackTrace();
                } finally {
                    ThriftConnectionPool.release(connection1);
                }
                long end = System.currentTimeMillis();
                System.out.println("1个push string cost：" + (end - begin) + "ms");
            }
        }).start();
    }

    private static void test() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threads);
        long begin = System.currentTimeMillis();
        syncCall(countDownLatch);
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println(num + "个push string cost：" + (end - begin) + "ms");
    }

    private static void test_2() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threads);
        long begin = System.currentTimeMillis();
        syncCall_2(countDownLatch);
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println(num + "个push long cost：" + (end - begin) + "ms");
    }

    private static void testBatch_2() throws InterruptedException {
        String cid = "6438994208421752841";
        String oid = "324058455";
        //ArrayList<String> list = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            QUEUE2.put(cid + "," + oid);
        }
        CountDownLatch countDownLatch1 = new CountDownLatch(threads);
        long begin1 = System.currentTimeMillis();
        syncCallBatch_2(countDownLatch1);
        countDownLatch1.await();
        long end1 = System.currentTimeMillis();
        System.out.println(pushed.get() + "个push cost：" + (end1 - begin1) + "ms");
    }

    private static void syncCallBatch_2(CountDownLatch countDownLatch) {
        for (int i = 0; i < threads; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> pushes = new ArrayList<>(batch);
                    ThriftPooledConnection connection = null;
                    try {
                        String cid;
                        connection = ThriftConnectionPool.getConnection();
                        PushThrift.Client client = connection.getClient();
                        while ((cid = QUEUE2.poll()) != null) {
                            if (pushes.size() < batch) {
                                pushes.add(cid);
                                if (pushes.size() == batch) {
                                    List<String> strings = client.checkAndPutIfAbsent(pushes);
                                    pushed.addAndGet(strings.size());
                                    pushes.clear();
                                } else {
                                    continue;
                                }
                            }
                        }
                        if (pushes.size() > 0) {
                            if (client != null) {
                                List<String> strings = client.checkAndPutIfAbsent(pushes);
                                pushed.addAndGet(strings.size());
                                pushes.clear();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        ThriftConnectionPool.release(connection);
                        countDownLatch.countDown();
                    }
                }
            };
            EXECUTOR_SERVICE.execute(runnable);
        }
    }

    private static void testBatch_1() throws InterruptedException {
        CountDownLatch countDownLatch1 = new CountDownLatch(threads);
        long begin1 = System.currentTimeMillis();
        syncCallBatch(countDownLatch1);
        countDownLatch1.await();
        long end1 = System.currentTimeMillis();
        System.out.println(pushed.get() + "个push cost：" + (end1 - begin1) + "ms");
    }

    private static void syncCallBatch(CountDownLatch countDownLatch) throws InterruptedException {
        for (int i = 0; i < threads; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ArrayList<Push> pushes = new ArrayList<>(batch);
                    ThriftPooledConnection connection = null;
                    try {
                        Push push;
                        connection = ThriftConnectionPool.getConnection();
                        PushThrift.Client client = connection.getClient();
                        while ((push = QUEUE.poll()) != null) {
                            if (pushes.size() < batch) {
                                pushes.add(push);
                                if (pushes.size() == batch) {
                                    if (client != null) {
                                        client.pushList(pushes);
                                        pushed.addAndGet(pushes.size());
                                        pushes.clear();
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }
                        if (pushes.size() > 0) {
                            if (client != null) {
                                client.pushList(pushes);
                                pushed.addAndGet(pushes.size());
                                pushes.clear();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        ThriftConnectionPool.release(connection);
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
                    ThriftPooledConnection connection = null;
                    try {
                        Push push;
                        while ((push = QUEUE.poll()) != null) {
                            Push finalPush = push;
                            connection = ThriftConnectionPool.getConnection();
                            PushThrift.Client client = connection.getClient();
                            if (client != null) {
                                //boolean push1 = client.push(finalPush);
                                boolean result = client.push(finalPush);
                                //System.out.println("返回：" + push1 + ",pushed=" + pushed.incrementAndGet());
                            }
                        }
                    } catch (TException e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                        ThriftConnectionPool.release(connection);
                    }
                }
            };
            EXECUTOR_SERVICE.execute(runnable);
        }
    }

    private static void syncCall_2(CountDownLatch countDownLatch) {
        for (int i = 0; i < threads; i++) {
            Runnable runnable = new Runnable() {
                long cid = 888888888888888l;
                int oid = 55555555;
                @Override
                public void run() {
                    ThriftPooledConnection connection = null;
                    try {
                        int i = 0;
                        while (i < 1) {
                            i++;
                            connection = ThriftConnectionPool.getConnection();
                            PushThrift.Client client = connection.getClient();
                            if (client != null) {
                                boolean result = client.pushLong(cid+i, oid);
                                //System.out.println("返回：" + push1 + ",pushed=" + pushed.incrementAndGet());
                            }
                        }
                    } catch (TException e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                        ThriftConnectionPool.release(connection);
                    }
                }
            };
            EXECUTOR_SERVICE.execute(runnable);
        }
    }
}
