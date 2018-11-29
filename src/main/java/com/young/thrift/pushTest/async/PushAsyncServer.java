package com.young.thrift.pushTest.async;

import com.young.thrift.pushTest.PushThrift;
import com.young.thrift.pushTest.sync.PushThriftImpl;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

public class PushAsyncServer {

    public static final int port = 9999;
    public static void main(String[] args){
        try {
            test1();
            //test2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test2() throws TTransportException {
        TProcessor tProcessor = new PushThrift.AsyncProcessor<PushThrift.AsyncIface>(new PushThriftAsyncImpl());
        TServerSocket serverSocket = new TServerSocket(port);
        TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverSocket);
        args.processor(tProcessor);
        args.protocolFactory(new TCompactProtocol.Factory());
        args.transportFactory(new TFastFramedTransport.Factory());
        TServer server = new TThreadPoolServer(args);
        System.out.println("Start thrift server at port : "+ port);
        server.serve();
    }

    private static void test1() throws TTransportException {
        TProcessor tprocessor = new PushThrift.AsyncProcessor<PushThrift.AsyncIface>(new PushThriftAsyncImpl());
        TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
        TThreadedSelectorServer.Args tArgs = new TThreadedSelectorServer.Args(serverTransport);
        tArgs.processor(tprocessor);
        tArgs.protocolFactory(new TCompactProtocol.Factory());
        tArgs.transportFactory(new TFastFramedTransport.Factory());
        TServer server = new TThreadedSelectorServer(tArgs);
        System.out.println("Start thrift server at port : "+ port);
        server.serve();
    }
}
