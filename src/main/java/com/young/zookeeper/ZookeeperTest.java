package com.young.zookeeper;

import org.apache.zookeeper.*;

public class ZookeeperTest {

    public static void main(String[] args) throws InterruptedException {
        final String path = "/learn";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ZooKeeper zk = new ZooKeeper("localhost", 3000, null);
                    zk.create(path, "haha".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                    System.out.println("created znode : "+path);
                    byte[] data = zk.getData(path, new Watcher() {
                        @Override
                        public void process(WatchedEvent event) {
                            System.out.println(event.toString());
                        }
                    }, null);
                    System.out.println(new String(data));
                    //zk.setData(path, "second".getBytes(), 1);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();

        Thread.sleep(200000);
    }
}
