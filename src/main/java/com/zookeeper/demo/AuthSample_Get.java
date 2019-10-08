package com.zookeeper.demo;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * @Description:一句话的功能说明
 * @Author: chendom
 * @Date: 2019/10/8 8:34
 * @Version 1.0
 */
public class AuthSample_Get {
private static final String PATH="/zk-book-auth-test";

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",50000,null);
        zooKeeper.addAuthInfo("digest","foo:true".getBytes());
        zooKeeper.create(PATH,"init".getBytes(),ZooDefs.Ids.CREATOR_ALL_ACL,CreateMode.EPHEMERAL);
        Thread.sleep(Integer.MAX_VALUE);

    }
}
