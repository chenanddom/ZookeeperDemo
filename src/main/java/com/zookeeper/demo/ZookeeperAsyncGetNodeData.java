package com.zookeeper.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

/**
 * @Description:异步获取数据节点的内容
 * @Author: chendom
 * @Date: 2019/9/26 8:42
 * @Version 1.0
 */
public class ZookeeperAsyncGetNodeData implements Watcher {
    private static CountDownLatch connetedSemphore = new CountDownLatch(1);

    private static ZooKeeper zooKeeper;
    public static void main(String[] args)throws Exception {
        String path = "/zk-book";
        zooKeeper = new ZooKeeper("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",
                5000,
                new ZookeeperAsyncGetNodeData());
        connetedSemphore.await();
        zooKeeper.create(path,"123".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
        zooKeeper.getData(path,true,null);
        //基于版本可以很好的控制zookeeper的节点的原子性(虽然这里是-1是不合法的，但是做了操作之后版本还是从0开始计算开始的)
        zooKeeper.setData(path,"content".getBytes(),-1,new IStatCallback(),null);

        Thread.sleep(5000);
    }
    public void process(WatchedEvent event) {
        if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && event.getPath() == null) {
                connetedSemphore.countDown();
            }
        }
    }
    static class IStatCallback implements AsyncCallback.StatCallback{
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            if (rc==0){
                System.out.println("SUCCESS");

                    try {
                        System.out.println("path:"+path+" content:"+new String(zooKeeper.getData(path,true,stat),"utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}
