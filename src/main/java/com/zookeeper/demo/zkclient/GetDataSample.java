package com.zookeeper.demo.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * @Description:一句话的功能说明
 * @Author: chendom
 * @Date: 2019/10/9 8:48
 * @Version 1.0
 */
public class GetDataSample {
    public static void main(String[] args) throws InterruptedException {
        String path = "/zk-book";
        ZkClient zkClient = new ZkClient("192.168.150.198:2181,192.168.150.198:2182,192.168.150.198:2183",5000);
        zkClient.createEphemeral(path,"123");
        zkClient.subscribeDataChanges(path,new IZkDataListener(){
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("Node "+s+" changed, new data:"+o);
            }

            public void handleDataDeleted(String s) throws Exception {
                System.out.println("Node "+s+" deleted.");
            }
        });
        System.out.println(zkClient.readData(path,true));
        zkClient.writeData(path,"456");
        Thread.sleep(1000);
        zkClient.delete(path);
        Thread.sleep(5000);

    }
}
