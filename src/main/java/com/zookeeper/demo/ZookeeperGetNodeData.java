package com.zookeeper.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Description:一句话的功能说明
 * @Author: chendom
 * @Date: 2019/9/25 9:06
 * @Version 1.0
 */
public class ZookeeperGetNodeData implements Watcher{
    private static CountDownLatch connectedStatedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk = null;
    private static Stat stat= new Stat();


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String path = "/zk-book";
        zk = new ZooKeeper("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",
                5000,
                new ZookeeperGetNodeData());
        connectedStatedSemaphore.await();
        zk.create(path,"123".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
        zk.getData(path,true,null);
        //基于版本可以很好的控制zookeeper的节点的原子性(虽然这里是-1是不合法的，但是做了操作之后版本还是从0开始计算开始的)
        Stat stat = zk.setData(path,"content".getBytes(),-1);
        System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());
        Stat stat2 = zk.setData(path,"456".getBytes(),stat.getVersion());
        System.out.println(stat2.getCzxid()+","+stat2.getMzxid()+","+stat2.getVersion());
        Thread.sleep(5000);
    }

    public void process(WatchedEvent event) {
        if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && event.getPath() == null) {
                connectedStatedSemaphore.countDown();
            } else if (event.getType() == Event.EventType.NodeChildrenChanged) {
                try {
                    System.out.println(new String(zk.getData(event.getPath(), true, stat)));
                    System.out.println(stat.getCzxid() + ","  + stat.getMzxid() + "," + stat.getVersion());
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
