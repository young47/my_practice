package com.young.nio.net;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 一个channel会与唯一一个selectKey绑定；每次register，其中的ops都是替换而不是取并集；
 * 比如先注册了op_read, 后面有注册了op_write事件，则该channel只绑定了op_write事件；
 * <p>
 * socketChannel.read()返回条件：1. 读到了socket缓存的末尾；2. byteBuffer没有可用的空间；3. socket关闭了
 * <p>
 *
 *
 *
 */
public class NioServer {
    public static int port = 8888;

    public static void main(String[] args) {
        server();
    }

    private static void server() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            Selector selector = Selector.open();
            SelectionKey serverSockKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            int selectCnt = 0;
            while (selector.select() > 0) {
                selectCnt++;
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();//这一句确保了select()没有事件可选；否则这个事件遗留在select()中且又没有改变，导致select()返回0
                    if (selectionKey.isAcceptable()) {
                        //iterator.remove();
                        ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = channel.accept();
                        socketChannel.configureBlocking(false);
                        //socketChannel与selectionKey1绑定
                        SelectionKey selectionKey1 = socketChannel.register(selector, SelectionKey.OP_READ);
                        //System.out.println("read key = "+selectionKey1);
                        System.out.println("Accept one client connection!");
                    } else if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        //System.out.println("RcvBuf = "+socketChannel.getOption(StandardSocketOptions.SO_RCVBUF));
                        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
                        int read = 0, loop = 0;
                        //System.out.println("selectCnt = "+selectCnt);
                        while ((read = socketChannel.read(byteBuffer)) > 0) {
                            System.out.println("Receive from client size = " + read + " , content = " + new String(byteBuffer
                                    .array()));
                            System.out.println("loopCnt = " + (++loop));
                            byteBuffer.clear();
                            byteBuffer.compact();
                            //byteBuffer = ByteBuffer.allocate(20);
                        }
                        //读完数据后，返回数据给客户端
                        /*if (!selectionKey.isWritable()) {
                            //这样会把read事件挤掉
                            SelectionKey register = socketChannel.register(selector, SelectionKey.OP_WRITE);
                            System.out.println("write key = "+register);
                            System.out.println("Set writable");
                            status(selectionKey);
                        }*/
                        //response(socketChannel);

                    } /*else if (selectionKey.isWritable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.wrap("Hi, client. I am from server".getBytes());
                        int write = socketChannel.write(byteBuffer);
                        ++writeCnt;
                        System.out.println("Has writed to client size = " + write * writeCnt + " ,writeCnt = " + writeCnt + " ," +
                                "socket send buff size = " + socketChannel.socket().getSendBufferSize());
                        //写完后，取消写事件，否则会不停得写，等client发来数据再回写
                        selectionKey.interestOps(selectionKey.interestOps() & ~SelectionKey.OP_WRITE);
                    }*/
                }
                //System.out.println("hahahah");
            }
            System.out.println("Over!!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void response(SocketChannel socketChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap("Hi, client. I am from server.".getBytes());
        int write = socketChannel.write(byteBuffer);
        //++writeCnt;
        System.out.println("Write to client size = " + write);
    }

    private static void status(SelectionKey selectionKey) {
        int interestSet = selectionKey.interestOps();
        boolean isInterestedInAccept = (interestSet & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT;
        boolean isInterestedInConnect = (interestSet & SelectionKey.OP_CONNECT) == SelectionKey.OP_CONNECT;
        boolean isInterestedInRead = (interestSet & SelectionKey.OP_READ) == SelectionKey.OP_READ;
        boolean isInterestedInWrite = (interestSet & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE;
        System.out.println(selectionKey + ", accept = " + isInterestedInAccept + ", connect = " + isInterestedInConnect + ", " +
                "read" +
                " = " +
                isInterestedInRead + ", write = " + isInterestedInWrite);

    }

   /* private int updateSelectedKeys() {
        int i = this.pollWrapper.updated;
        int j = 0;
        for (int k = 0; k < i; k++) {
            int m = this.pollWrapper.getDescriptor(k);
            SelectionKeyImpl localSelectionKeyImpl = (SelectionKeyImpl) this.fdToKey.get(Integer.valueOf(m));
            if (localSelectionKeyImpl != null) {
                int n = this.pollWrapper.getEventOps(k);
                if (this.selectedKeys.contains(localSelectionKeyImpl)) {
                    if (localSelectionKeyImpl.channel.translateAndSetReadyOps(n, localSelectionKeyImpl)) j++;
                } else {
                    localSelectionKeyImpl.channel.translateAndSetReadyOps(n, localSelectionKeyImpl);
                    if ((localSelectionKeyImpl.nioReadyOps() & localSelectionKeyImpl.nioInterestOps()) != 0) {
                        this.selectedKeys.add(localSelectionKeyImpl);
                        j++;
                    }
                }
            }
        }
        return j;
    }*/
}
