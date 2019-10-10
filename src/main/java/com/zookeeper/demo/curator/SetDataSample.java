package com.zookeeper.demo.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @Description:一句话的功能说明
 * @Author: chendom
 * @Date: 2019/10/10 8:50
 * @Version 1.0
 */
public class SetDataSample {
    private static String path="/zk-book";
    private static CuratorFramework client = CuratorFrameworkFactory
            .builder()
            .connectString("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();

    public static void main(String[] args) throws Exception {
        client.start();
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,"this is content".getBytes());
        Thread.sleep(1000);
        Stat stat = new Stat();
        new String(client.getData().storingStatIn(stat).forPath(path));
        System.out.println("Success set node for:"+path+", new version:"+client.setData().withVersion(stat.getVersion()).forPath(path).getVersion());

        client.setData().withVersion(stat.getVersion()).forPath(path);


        System.out.println();
    }
}
