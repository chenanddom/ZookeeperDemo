package com.zookeeper.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Description:获取子节点的代码
 * @Author: chendom
 * @Date: 2019/9/25 8:27
 * @Version 1.0
 */
public class ZookeeperGetChildNode implements Watcher {
    private static CountDownLatch connectedStatedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeper=null;
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
       String path = "/zk-book";
        zooKeeper = new ZooKeeper("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",
                5000,
                new ZookeeperGetChildNode());
        connectedStatedSemaphore.await();
        zooKeeper.create(path,"content1".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        zooKeeper.create(path+"/c1","content2".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);



//        List<String> childrenList = zooKeeper.getChildren(path,true);
//        System.out.println(childrenList);
        zooKeeper.getChildren(path,true,new IChildren2Callback(),null);
        zooKeeper.create(path+"/c2","content3".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
        Thread.sleep(100000);
    }

    public void process(WatchedEvent event) {
        System.out.println("Receive watched event：" + event);
        if (Event.KeeperState.SyncConnected==event.getState()) {
            if (Event.EventType.None == event.getType() && event.getPath() == null) {
                connectedStatedSemaphore.countDown();
            } else if (event.getType() == Event.EventType.NodeChildrenChanged) {
                try {
                    System.out.println("ReGetChild:" + zooKeeper.getChildren(event.getPath(), true));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 异步获取节点的信息
     */
    static class IChildren2Callback implements  AsyncCallback.Children2Callback{

        public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
            System.out.println("Get Child znode result:[response code:"+rc+",param path: "+path
                    +", ctx: "+ctx+",children list: "+children+",stat: "+stat+"]");
        }
    }

}
