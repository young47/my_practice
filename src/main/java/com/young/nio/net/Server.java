package com.young.nio.net;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int port = 8080;

    private static void init() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(port));
            System.out.println("server-socket localport = " + serverSocket.getLocalPort());

            int j = 0;
            Socket socket = serverSocket.accept();
            //socket.setSoLinger(true, 1);
            socket.setReceiveBufferSize(1);
            System.out.println("real rec buf size = "+socket.getReceiveBufferSize());
            System.out.println("server remote port = " + socket.getPort() + ", localport = " + socket.getLocalPort());
            while (true) {
                Thread.sleep(100);

                InputStream inputStream = socket.getInputStream();
                byte[] bytes = new byte[128];
                int readed = inputStream.read(bytes);
                if (readed > 0) {
                    String data = new String(bytes);
                    System.out.println("read data from client : " + data);
                } else {
                    //关闭serversocket，client依然会源源不断的发来数据
                    //关闭socket后，两端立刻都停止了
                    serverSocket.close();
                    System.out.println(" ");
                    System.out.println("shutdown socket...");
                    socket.close();
                    System.out.println("socket.isInputShutdown():" + socket.isInputShutdown() + " | socket.isOutputShutdown():" +
                            socket.isOutputShutdown() + " | socket.isClosed():" + socket.isClosed());
                    break;
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("over...");

    }

    private static void start() {

    }

    public static void main(String[] args) {
        Server.init();
        Server.start();
    }
}
