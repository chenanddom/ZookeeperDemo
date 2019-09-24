package com.zookeeper.demo;


import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Description:一句话的功能说明
 * @Author: chendom
 * @Date: 2019/9/24 8:30
 * @Version 1.0
 */
public class ZookeeperCreateNode implements Watcher {

    private static CountDownLatch connectedStatedSemaphore = new CountDownLatch(1);


    public static void main(String[] args) throws Exception{
ZooKeeper zooKeeper = new ZooKeeper("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",
                                        5000,
                                    new ZookeeperCreateNode());

    connectedStatedSemaphore.await();
//    String path = zooKeeper.create("/zk-test-emhemeral-",
//            "".getBytes(),
//            ZooDefs.Ids.OPEN_ACL_UNSAFE,
//            CreateMode.EPHEMERAL);
//        System.out.println("Success create znode:"+path);
//
//        String path2 = zooKeeper.create("/zk-test-emhemeral-",
//                "".getBytes(),
//                ZooDefs.Ids.OPEN_ACL_UNSAFE,
//                CreateMode.EPHEMERAL_SEQUENTIAL);
//
//        System.out.println("Success create znode:"+path2);
//        String path3 = zooKeeper.create("/zk-test-emhemeral-1",
//                "just test".getBytes(),
//                ZooDefs.Ids.OPEN_ACL_UNSAFE,
//                CreateMode.PERSISTENT);
//        System.out.println("path3:"+path3);
//        使用回调得方式获取结果创建节点
        zooKeeper.create("/zk-test-emhemeral-","11111".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL,new IStringCallback(),"I am context");
        Thread.sleep(5000);
    }

    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected==watchedEvent.getState()){
            connectedStatedSemaphore.countDown();
        }
    }
    static class IStringCallback implements AsyncCallback.StringCallback{

        public void processResult(int i, String s, Object o, String s1) {
            System.out.println("rc:"+i+" path4:"+s+" ctx"+o+" real path name:"+s1);
        }
    }
}


