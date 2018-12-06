package com.young.thrift;

import com.young.thrift.pushTest.PushThrift;
import com.young.thrift.pushTest.sync.PushServer;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThriftConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(ThriftConnectionPool.class);
    private static final int _1MIN = 60 * 1000;
    private static final int _1SECOND = 1 * 1000;
    private static int maxConnections = 2000;
    private static int minConnections = 20;
    private static int idleTimeOut = 3 * _1MIN;
    private static int validateAfterInactivity = 30 * _1SECOND;

    private static final ReentrantLock GetClientLock = new ReentrantLock(false);
    private static final Condition notEmpty = GetClientLock.newCondition();
    private static final Condition empty = GetClientLock.newCondition();

    private static final List<ThriftPooledConnection> available = new ArrayList<>(200);
    private static final List<ThriftPooledConnection> lent = new ArrayList<>(100);

    static {
        Thread createThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        GetClientLock.lock();
                        if (available.size() > 0) {
                            empty.await();
                        }

                        ThriftPooledConnection pooledConnection = createNewConnection();
                        available.add(pooledConnection);
                        notEmpty.signalAll();
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
        createThread.setDaemon(true);
        createThread.start();
        logger.info("Thread [to create thrift client] begins!");
    }

    public static ThriftPooledConnection getConnection() {
        return getConnection(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    public static ThriftPooledConnection getConnection(long timeout, TimeUnit timeUnit) {
        for (; ; ) {
            try {
                GetClientLock.lock();
                int availableSize = available.size();
                int lentSize = lent.size();
                if (availableSize == 0) {
                    if (lentSize < maxConnections) {
                        empty.signalAll();
                    }
                    long remainingToWait = notEmpty.awaitNanos(timeout);
                    timeout = remainingToWait;
                    if (remainingToWait <= 0L) {
                        throw new RuntimeException("Get thrift client timeout!");
                    }
                } else {
                    ThriftPooledConnection pooledConnection = available.remove(availableSize - 1);
                    if (System.currentTimeMillis() - pooledConnection.getLastActiveTimeMillis() >= idleTimeOut) {
                        pooledConnection.gettTransport().close();
                        pooledConnection = null;
                        continue;
                    }
                    if (validateAfterInactivity > 0) {
                        if (System.currentTimeMillis() - pooledConnection.getLastActiveTimeMillis() >= validateAfterInactivity) {
                            if (!validate(pooledConnection)) {
                                close(pooledConnection);
                                continue;
                            }
                        }
                    }
                    lent.add(pooledConnection);
                    return pooledConnection;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Get thrift client timeout!", e);
            } finally {
                GetClientLock.unlock();
            }
        }
    }

    private static void close(ThriftPooledConnection pooledConnection) {
        pooledConnection.gettTransport().close();
        pooledConnection = null;
    }

    private static boolean validate(ThriftPooledConnection pooledConnection) {
        if (pooledConnection.gettTransport().isOpen()) {
            //这种需要再检查一次，因为服务端的连接可能已经断开
            try {
                return pooledConnection.getClient().ping();
            } catch (Exception e) {
                logger.error("ping() error! this socket had been closed!");
                return false;
            }
        } else {
            return false;
        }
    }

    private static ThriftPooledConnection createNewConnection() throws TTransportException {
        TTransport transport = new TFastFramedTransport(new TSocket("10.2.132.127", PushServer.port));
        //TTransport transport = new TSocket("10.2.131.165", PushServer.port);
        TProtocol protocol = new TCompactProtocol(transport);
        PushThrift.Client client = new PushThrift.Client(protocol);
        transport.open();
        return new ThriftPooledConnection(client, transport, System.currentTimeMillis());
    }

    public static void release(ThriftPooledConnection pooledConnection) {
        if (pooledConnection == null) {
            return;
        }
        try {
            GetClientLock.lock();
            lent.remove(pooledConnection);
            pooledConnection.setLastActiveTimeMillis(System.currentTimeMillis());
            available.add(pooledConnection);
            notEmpty.signalAll();
        } finally {
            GetClientLock.unlock();
        }
    }

}
