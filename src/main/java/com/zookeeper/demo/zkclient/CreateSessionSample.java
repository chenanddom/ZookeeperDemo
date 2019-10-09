package com.zookeeper.demo.zkclient;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Description:一句话的功能说明
 * @Author: chendom
 * @Date: 2019/10/9 9:02
 * @Version 1.0
 */
public class CreateSessionSample {

    public static void main(String[] args) throws InterruptedException {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        //创建一个客户端实例，单实际上是没有完成会话的创建的，需要调用start()之后才算是完成会话的创建
        CuratorFramework client = CuratorFrameworkFactory
                .newClient("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",5000,3000,retryPolicy);
        //创建会话
        client.start();
        Thread.sleep(5000);
    }
}
