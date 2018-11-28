package com.young.thrift;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ThriftClient {

    private static final String push = "{\"cid\":\"6403918693415694448\",\"intervalTime\":\"2700\",\"isFilter\":0," +
            "\"oidList\":[\"323685492\",\"323625170\",\"323512849\",\"323509584\",\"323651951\",\"323481442\",\"323336126\"," +
            "\"322384117\",\"323120782\",\"323689875\",\"323597913\",\"323384955\",\"323395620\",\"323209329\",\"323469477\"," +
            "\"323379267\",\"323651141\",\"323615056\",\"323205261\",\"323474071\"],\"type\":\"h\"," +
            "\"userInfo\":\"{\\\"a\\\":36,\\\"ap\\\":42,\\\"c\\\":6403918693415694448,\\\"ch\\\":3310," +
            "\\\"dt\\\":\\\"0867910038477404200000058800CN01\\\",\\\"f\\\":2,\\\"im\\\":\\\"867910038477404\\\",\\\"p\\\":1," +
            "\\\"pm\\\":0,\\\"pt\\\":\\\"d75edde66ff34479eb31d1198dc9563c0e8897bd\\\",\\\"pv\\\":0,\\\"s\\\":1,\\\"st\\\":0," +
            "\\\"t\\\":0,\\\"tm\\\":0,\\\"v\\\":613}\"}";
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(20);
    private static final AtomicInteger output = new AtomicInteger(0);

    public static void main(String[] args) throws TException {
        //TTransport transport = new TSocket("10.2.131.165", 9990);
        TTransport transport = new TSocket("127.0.0.1", 9990);
        TProtocol protocol = new TBinaryProtocol(transport);
        WorkerThrift.Client client = new WorkerThrift.Client(protocol);
        transport.open();
        if (client.submitPushJob(push)) {
            System.out.println("success");
        }
        //syncCall();
        //asyncCall();
    }

    private static void asyncCall() {
        long begin = System.currentTimeMillis();
        EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TAsyncClientManager clientManager = new TAsyncClientManager();
                    TNonblockingTransport transport = new TNonblockingSocket("10.2.131.165", 9990, 2000);
                    TBinaryProtocol.Factory factory = new TBinaryProtocol.Factory();
                    WorkerThrift.AsyncClient client = new WorkerThrift.AsyncClient(factory, clientManager, transport);
                    //transport.open();
                    while (true) {
                        client.submitPushJob(push, new AsyncMethodCallback() {
                            @Override
                            public void onComplete(Object o) {
                                System.out.println("haha");
                            }

                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                            }
                        });
                        int i = output.incrementAndGet();
                        System.out.println("output=" + i);
                        if (i == 5000) {
                            final long end = System.currentTimeMillis();
                            System.out.println("cost:" + (end - begin) + "ms");
                            return;
                        }
                    }
                } catch (TTransportException e) {
                    e.printStackTrace();
                } catch (TException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private static void syncCall() {
        long begin = System.currentTimeMillis();
        EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TTransport transport = new TSocket("10.2.131.165", 9990);
                    TProtocol protocol = new TBinaryProtocol(transport);
                    WorkerThrift.Client client = new WorkerThrift.Client(protocol);
                    transport.open();
                    while (true) {
                        if (client.submitPushJob(push)) {
                            System.out.println("success");
                            int i = output.incrementAndGet();
                            System.out.println("output=" + i);
                            if (i == 5000) {
                                final long end = System.currentTimeMillis();
                                System.out.println("cost:" + (end - begin) + "ms");
                                return;
                            }
                        }
                    }
                } catch (TTransportException e) {
                    e.printStackTrace();
                } catch (TException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
