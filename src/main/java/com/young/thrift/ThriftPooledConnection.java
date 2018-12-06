package com.young.thrift;

import com.young.thrift.pushTest.PushThrift;
import org.apache.thrift.transport.TTransport;

public class ThriftPooledConnection {
    private PushThrift.Client client;
    private TTransport tTransport;
    private long lastActiveTimeMillis;
    private long createTimeMillis = System.currentTimeMillis();

    public ThriftPooledConnection(PushThrift.Client client, TTransport tTransport, long lastActiveTimeMillis) {
        this.client = client;
        this.tTransport = tTransport;
        this.lastActiveTimeMillis = lastActiveTimeMillis;
    }

    public PushThrift.Client getClient() {
        return client;
    }

    public void setClient(PushThrift.Client client) {
        this.client = client;
    }

    public TTransport gettTransport() {
        return tTransport;
    }

    public void settTransport(TTransport tTransport) {
        this.tTransport = tTransport;
    }

    public long getLastActiveTimeMillis() {
        return lastActiveTimeMillis;
    }

    public void setLastActiveTimeMillis(long lastActiveTimeMillis) {
        this.lastActiveTimeMillis = lastActiveTimeMillis;
    }
}
