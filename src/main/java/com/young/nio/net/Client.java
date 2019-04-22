package com.young.nio.net;


import java.net.Socket;

public class Client {
    private static void init() {
        Socket socket = null;
        try {
            //socket = new Socket("127.0.0.1", Server.port);
            socket = new Socket("10.13.89.46", 19999);
            System.out.println("client remote port = " + socket.getPort() + ", localport = " + socket.getLocalPort());
            //socket.setSendBufferSize(3);
            System.out.println("real send buf size = "+socket.getSendBufferSize());
            //socket.setTcpNoDelay(false);
            int i = 0;
            byte[] bytes = "AUTH".getBytes();
            while (i<1) {
                //byte[] bytes = String.valueOf(i).getBytes();
                socket.getOutputStream().write(bytes);
                System.out.println("i=" + (++i) + "," + bytes.length);
                System.out.println("socket.isInputShutdown():" + socket.isInputShutdown()+" | socket.isOutputShutdown():" +
                        socket.isOutputShutdown()+" | socket.isClosed():" + socket.isClosed());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Over");
            System.out.println("socket.isInputShutdown():" + socket.isInputShutdown()+" | socket.isOutputShutdown():" +
                    socket.isOutputShutdown()+" | socket.isClosed():" + socket.isClosed());
        }

    }

    public static void main(String[] args) {
        Client.init();
    }
}
