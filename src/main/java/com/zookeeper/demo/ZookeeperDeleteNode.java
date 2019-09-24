package com.zookeeper.demo;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Description:一句话的功能说明
 * @Author: chendom
 * @Date: 2019/9/24 12:40
 * @Version 1.0
 */
public class ZookeeperDeleteNode implements Watcher{
    private static CountDownLatch connectedStatedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        ZooKeeper zooKeeper = new ZooKeeper("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",
                5000,
                new ZookeeperCreateNode());
//        zooKeeper.delete("/zk-demo0000000024",0);
        zooKeeper.delete("/zk-demo0000000025", 0, new AsyncCallback.VoidCallback() {
            public void processResult(int i, String s, Object o) {
                System.out.println("result code:"+i+" path:"+s+" context:"+o);
            }
        },"this is context");
        Thread.sleep(10000);
    }
    public void process(WatchedEvent watchedEvent) {
        if (Watcher.Event.KeeperState.SyncConnected==watchedEvent.getState()){
            connectedStatedSemaphore.countDown();
        }
    }
}
