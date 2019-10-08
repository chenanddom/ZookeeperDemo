package com.zookeeper.demo;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Description:一句话的功能说明
 * @Author: chendom
 * @Date: 2019/10/8 8:34
 * @Version 1.0
 */
public class AuthSample {
private static final String PATH="/zk-book-auth-test";
private static final String PATH2=PATH+"/child";
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",50000,null);
        zooKeeper.addAuthInfo("digest","foo:true".getBytes());
        if (zooKeeper.exists(PATH,null)!=null) {
            zooKeeper.delete(PATH, -1);
        }
        zooKeeper.create(PATH, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
//        semaphore.await();
        Thread.sleep(5000);
        zooKeeper.create(PATH2,"child".getBytes(),ZooDefs.Ids.CREATOR_ALL_ACL,CreateMode.EPHEMERAL);
    //没有权限的获取zookeeper节点的数据信息
//        ZooKeeper zooKeeper2 = new ZooKeeper("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",50000,null);
//        zooKeeper2.getData(PATH,false,null);


//        ZooKeeper zooKeeper3 = new ZooKeeper("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",50000,null);
//        zooKeeper3.addAuthInfo("digest","foo:false".getBytes());
//        byte[] data = zooKeeper3.getData(PATH, false, null);
//        System.out.println(new String(data));
        /**
         * 删除的时候需要注意，对于删除而言，当客户端对数据节点添加额权限信息，客户端依然可以自由的删除该节点，但是对于子节点就需要添加相应的权限信息
         */
        ZooKeeper zooKeeper3 = new ZooKeeper("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",50000,null);
        zooKeeper3.addAuthInfo("digest","foo:true".getBytes());
        zooKeeper3.delete(PATH2,-1);
        zooKeeper3.delete(PATH,-1);

//        semaphore.countDown();
    }
}
