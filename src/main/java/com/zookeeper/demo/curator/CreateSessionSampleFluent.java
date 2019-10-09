package com.zookeeper.demo.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Description:一句话的功能说明
 * @Author: chendom
 * @Date: 2019/10/9 9:18
 * @Version 1.0
 */
public class CreateSessionSampleFluent {

    public static void main(String[] args) throws InterruptedException {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                                                         .connectString("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183")
                                                         .sessionTimeoutMs(5000)
                                                        .retryPolicy(retryPolicy)
                                                        .build();
        client.start();
        Thread.sleep(5000);
    }

}
