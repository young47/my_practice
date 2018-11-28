package com.young.thrift.pushTest;

import com.young.thrift.WorkerThrift;
import com.young.thrift.WorkerThriftImpl;
import com.young.thrift.module.Push;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerSocket;

public class PushServer {

    public static final int port = 9999;
    public static void main(String[] args){
        try {
            TProcessor tprocessor = new PushThrift.Processor<PushThrift.Iface>(new PushThriftImpl());
            TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
            TThreadedSelectorServer.Args tArgs = new TThreadedSelectorServer.Args(serverTransport);
            tArgs.processor(tprocessor);
            tArgs.protocolFactory(new TCompactProtocol.Factory());
            TServer server = new TThreadedSelectorServer(tArgs);
            System.out.println("Startint thrift server at port : "+ port);
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
