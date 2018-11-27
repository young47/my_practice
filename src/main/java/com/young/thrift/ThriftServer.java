package com.young.thrift;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

public class ThriftServer {
    public static void main(String[] args) {
        try {
            TProcessor tprocessor = new WorkerThrift.Processor<WorkerThrift.Iface>(
                    new WorkerThriftImpl());
            TServerSocket serverTransport = new TServerSocket(9999);
            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(serverTransport);
            tArgs.processor(tprocessor);
            tArgs.protocolFactory(new TBinaryProtocol.Factory());
            TServer server = new TThreadPoolServer(tArgs);
            System.out.println("Startint thrift server at port : "+ 9999);
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
