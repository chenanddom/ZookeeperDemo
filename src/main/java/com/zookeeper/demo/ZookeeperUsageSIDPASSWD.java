package com.zookeeper.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Description:一句话的功能说明
 * @Author: chendom
 * @Date: 2019/9/23 8:58
 * @Version 1.0
 */
public class ZookeeperUsageSIDPASSWD implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",5000,new ZookeeperUsageSIDPASSWD());
        long sessionId = zooKeeper.getSessionId();
        byte[] sessionPasswd = zooKeeper.getSessionPasswd();
        System.out.println("sessionId:"+sessionId);
        System.out.println("passwd:"+new String(sessionPasswd,"UTF-8"));

        //使用错误的sessionId和sessionPassword
        zooKeeper = new ZooKeeper("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",
                5000,
                new ZookeeperUsageSIDPASSWD(),
                1L,
                "test".getBytes());
        zooKeeper = new ZooKeeper("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",
                                    5000,
                                    new ZookeeperUsageSIDPASSWD(),
                                    sessionId,
                                    sessionPasswd);
        Thread.sleep(5000);
//        connectedSemaphore.await();
    }

    public void process(WatchedEvent event) {
        System.out.println("Receive watched event：" + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
