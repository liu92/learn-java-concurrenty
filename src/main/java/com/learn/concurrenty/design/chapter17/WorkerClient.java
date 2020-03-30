package com.learn.concurrenty.design.chapter17;

/**
 * @ClassName: WorkerClient
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 10:26
 * History:
 * @<version> 1.0
 */
public class WorkerClient {
    public static void main(String[] args) {
        final  Channel channel = new Channel(5);
        //传送带启动
        channel.startWorker();

        new TransportThread("Alex", channel).start();
        new TransportThread("Jack", channel).start();
        new TransportThread("wil", channel).start();
    }
}
