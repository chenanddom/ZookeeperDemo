package com.zookeeper.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Description:一句话的功能说明
 * @Author: chendom
 * @Date: 2019/9/18 9:20
 * @Version 1.0
 */
public class ZookeeperSample implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);


    public static void main(String[] args) {

        try {
            ZooKeeper zookeeper = new ZooKeeper("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",     5000, new ZookeeperSample());
            System.out.println(zookeeper.getState());
            connectedSemaphore.await();
        } catch (InterruptedException e) {} catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ZooKeeper session established.");
    }



    public void process(WatchedEvent event) {
        System.out.println("Receive watched event：" + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
