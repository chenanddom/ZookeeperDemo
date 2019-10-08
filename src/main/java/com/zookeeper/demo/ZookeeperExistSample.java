package com.zookeeper.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Description:一句话的功能说明
 * @Author: chendom
 * @Date: 2019/9/27 8:41
 * @Version 1.0
 */
public class ZookeeperExistSample implements Watcher {

    private static CountDownLatch connectedStatedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
    String path = "/zk-book";
        zooKeeper = new ZooKeeper("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",
                5000,
                new ZookeeperExistSample());
        connectedStatedSemaphore.await();
        zooKeeper.exists(path,true);
        zooKeeper.create(path,"".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        zooKeeper.setData(path,"123".getBytes(),-1);
        String childNodePath = path+"/c1";
        //临时节点下无法创建子节点
        zooKeeper.exists(path+"/c1",true);
        zooKeeper.create(path+"/c1","".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        zooKeeper.setData(path+"/c1","456".getBytes(),-1);
        zooKeeper.delete(path+"/c1",-1);
        zooKeeper.delete(path,-1);
        Thread.sleep(5000);

    }

    public void process(WatchedEvent event) {
                try {
                    if (Event.KeeperState.SyncConnected == event.getState()) {
                        if (Event.EventType.None == event.getType() && null == event.getPath()) {
                            connectedStatedSemaphore.countDown();
                        } else if (Event.EventType.NodeCreated == event.getType()) {
                            System.out.println("Node(" + event.getPath() + ")Created");
                           zooKeeper.exists(event.getPath(), true);
//                            System.out.println();
                        }else if (Event.EventType.NodeDeleted==event.getType()){
                            System.out.println("Node("+event.getPath()+")Deleted");
                            zooKeeper.exists(event.getPath(),true);
                        }else if (Event.EventType.NodeDataChanged==event.getType()){
                            System.out.println("Node("+event.getPath()+")DataChanged");
                            zooKeeper.exists(event.getPath(),true);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
    }
}
