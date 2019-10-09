package com.zookeeper.demo.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * @Description:一句话的功能说明
 * @Author: chendom
 * @Date: 2019/10/9 8:30
 * @Version 1.0
 */
public class GetChildrenSample {

    public static void main(String[] args) throws InterruptedException {
    String path = "/zk-book";
        ZkClient zkClient = new ZkClient("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",5000);
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println(s+" 's child changed.currentChilds:"+list);
            }
        });
        zkClient.createPersistent(path);
        Thread.sleep(1000);
        System.out.println(zkClient.getChildren(path));
        zkClient.createPersistent(path+"/c1");
        Thread.sleep(1000);
        zkClient.delete(path+"/c1");
        Thread.sleep(1000);
        zkClient.delete(path);
        Thread.sleep(50000);
    }
}
