package com.young.thrift;

import com.young.thrift.pushTest.PushThrift;
import com.young.thrift.pushTest.sync.PushServer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThriftConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(ThriftConnectionPool.class);
    private static final int _1MIN = 60 * 1000;
    private static int maxConnections = 2000;
    private static int idleTime = 2 * _1MIN;

    private static final ReentrantLock GetClientLock = new ReentrantLock(false);
    private static final Condition notEmpty = GetClientLock.newCondition();
    private static final Condition empty = GetClientLock.newCondition();

    private static final List<PushThrift.Client> available = new ArrayList<>(200);
    private static final List<PushThrift.Client> lent = new ArrayList<>(100);


    private static final Thread createThread = new Thread();

    static {
        Thread createThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        GetClientLock.lock();
                        //每次都先创建一个
                        PushThrift.Client newClient = createNewClient();
                        available.add(newClient);
                        notEmpty.signalAll();
                        empty.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (TTransportException e) {
                        e.printStackTrace();
                    } finally {
                        GetClientLock.unlock();
                    }
                }

            }
        });
        createThread.start();
        createThread.setDaemon(true);
        logger.info("Thread [to create thrift client] begins!");
    }

    public static PushThrift.Client getClient() {
        return getClient(0L, TimeUnit.MILLISECONDS);
    }

    public static PushThrift.Client getClient(long timeout, TimeUnit timeUnit) {
        for (; ; ) {
            try {
                long remainingToWait = timeUnit.toNanos(timeout);
                GetClientLock.lock();
                int availableSize = available.size();
                int lentSize = lent.size();
                if (availableSize == 0) {
                    if (lentSize < maxConnections) {
                        empty.signalAll();
                    }
                    remainingToWait = notEmpty.awaitNanos(remainingToWait);
                    timeout = remainingToWait;
                    if (remainingToWait <= 0L) {
                        throw new RuntimeException("Get thrift client timeout");
                    }
                } else {
                    PushThrift.Client client = available.remove(availableSize - 1);
                    if (isClosed(client)) {
                        continue;
                    }
                    lent.add(client);
                    return client;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Get thrift client timeout");
            } finally {
                GetClientLock.unlock();
            }
        }
    }

    private static boolean isClosed(PushThrift.Client client) {
        try {
            return client.ping();
        } catch (TException e) {
            return false;
        }
    }

    private static PushThrift.Client createNewClient() throws TTransportException {
        TTransport transport = new TFastFramedTransport(new TSocket("10.2.131.165", PushServer.port));
        TProtocol protocol = new TCompactProtocol(transport);
        PushThrift.Client client = new PushThrift.Client(protocol);
        transport.open();
        return client;
    }

    private static void release(PushThrift.Client client) {
        if (client == null) {
            return;
        }
        try {
            GetClientLock.lock();
            lent.remove(client);
            available.add(client);
            notEmpty.signalAll();
        } finally {
            GetClientLock.unlock();
        }
    }


}
