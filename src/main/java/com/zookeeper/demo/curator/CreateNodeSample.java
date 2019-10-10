package com.zookeeper.demo.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @Description:一句话的功能说明
 * @Author: chendom
 * @Date: 2019/10/9 12:39
 * @Version 1.0
 */
public class CreateNodeSample {
    static String path = "/zk-book/c1";
    static CuratorFramework client = CuratorFrameworkFactory
            .builder()
            .connectString("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();
    public static void main(String[] args) throws Exception {
        client.start();
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path,"init".getBytes());

    }
}
