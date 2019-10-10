package com.zookeeper.demo.curator.asynchronous;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.*;

/**
 * @Description:一句话的功能说明
 * @Author: chendom
 * @Date: 2019/10/10 9:14
 * @Version 1.0
 */
public class CreateNodeBackgroundSample {
    private static String path = "/zk-book";
    private static CuratorFramework client = CuratorFrameworkFactory
            .builder()
            .connectString("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();
    private static CountDownLatch semphore = new CountDownLatch(2);
    //这种方式已经不推荐使用，而是使用手动创建线程池的方式，因为这种方式可能会耗尽内存(这个地方是使用new线程池的方式，如果有大量的请求就会创建大量的线程池，如果线程池对象管理不好就会出现内存溢出)
//    private static ExecutorService tp = Executors.newFixedThreadPool(2);
    private static ExecutorService tp =new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    public static void main(String[] args) throws Exception {
    client.start();
        System.out.println("Main thread:"+Thread.currentThread().getName());
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {

                System.out.println("event[code:"+curatorEvent.getResultCode()+", type:"+curatorEvent.getType()+"]");
                System.out.println("Thread of processResult:"+Thread.currentThread().getName());
                semphore.countDown();
            }
        },tp).forPath(path,"content".getBytes());

        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {

                System.out.println("event[code:"+curatorEvent.getResultCode()+", type:"+curatorEvent.getType()+"]");
                System.out.println("Thread of processResult:"+Thread.currentThread().getName());
                semphore.countDown();
            }
        }).forPath(path,"content2".getBytes());
        semphore.await();
        tp.shutdown();
    }
}
