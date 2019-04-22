package com.young.nio.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 对于可写事件，除非对方tcp的滑动窗口等于0或者发送缓存满了，否则select()每次的返回值总是大于0；
 * 对于可读事件，则要取决于对方是否发来数据，否则select()返回的事件里没有可读事件；
 * <p>
 * 一个socket一旦被创建，就是可写的，但不一定可读(得有人发来数据)，因此在nio编程中，一般不主动注册OP_WRITE事件，而是要注册OP_READ；
 * 那么什么时候注册OP_WRITE事件呢？一般是在socket缓存区已满或网络有问题等情况下或read完要response了才注册这种事件，这样就可以在可写的时候通知我们
 */
public class NioClient {

    public static void main(String[] args) {
        startClient();
    }

    private static void startClient() {
        SocketChannel channel = null;
        try {
            Selector selector = Selector.open();
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(InetAddress.getLocalHost(), NioServer.port));
            //channel.connect(new InetSocketAddress("10.13.89.46", NioServer.port));
            channel.setOption(StandardSocketOptions.SO_SNDBUF, 165536);
            System.out.println("SendBuf = "+channel.getOption(StandardSocketOptions.SO_SNDBUF));
            while (!channel.finishConnect()) {
                System.out.println("Connected to server!");
                sleep(1);
            }
            //连接后，可以向server写数据了
            //一般不注册写事件
            //channel.register(selector, SelectionKey.OP_WRITE);
            channel.register(selector, SelectionKey.OP_READ);

            sendMsgToServer(channel);
            int n = 0;
            while (selector.select() > 0 && (n++) < 1) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isReadable()) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
                        //byteBuffer.rewind();
                        int read = channel.read(byteBuffer);
                        System.out.println("Receive from server size = " + read + " , content = " + new String(byteBuffer.array
                                ()));
                        //等待一段时间，测试服务端write是否阻塞
                        /*if (read <= 0) {
                            sleep(10000);
                        }*/

                        //
                        //selectionKey.interestOps(selectionKey.interestOps() & ~SelectionKey.OP_READ);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(1000);
                channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static void sendMsgToServer(SocketChannel channel) throws IOException {
        String content = "Hi, server. I am from client.";
        StringBuilder sb = new StringBuilder(content);
       /* for (int i = 0; i < 1000; i++) {
            sb.append(content);
        }*/
        int write = channel.write(ByteBuffer.wrap(sb.toString().getBytes()));
        System.out.println("Write to server size = " + write);
        //等待一段时间，测试服务端read是否阻塞
        //sleep(100000);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

