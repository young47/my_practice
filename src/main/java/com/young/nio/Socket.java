package com.young.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by young on 17/11/14.
 */
public class Socket {
    static public void main(String[] args){
        try {
            Selector selector = Selector.open();
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            
            System.out.println(Long.MAX_VALUE);
            System.out.println(6381521720150765691l);

            System.out.println(Long.MAX_VALUE - 6381521720150765691l);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
