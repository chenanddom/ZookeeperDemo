package com.zookeeper.demo.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @Description: 使用Curator删除一个节点
 * @Author: chendom
 * @Date: 2019/10/10 8:34
 * @Version 1.0
 */
public class DeleteNodeSample {
    private static String path = "/zk-book/c1";
    private static CuratorFramework client = CuratorFrameworkFactory
            .builder()
            .connectString("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();
    public static void main(String[] args) throws Exception {

        client.start();
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,"content".getBytes());
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath(path);
        System.out.println(stat.toString());
        client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);
        //该方法会强行的删除，只要客户端会话有效，那么就会返回的重试，知道删除所有的节点。
//        client.delete().guaranteed().forPath(path);
    }
}
