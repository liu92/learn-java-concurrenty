package com.learn.concurrenty.design.chapter9;

/**
 *  线程挂起
 * 比如：你在做饭，但是你的快递到了，这个时候没有办法去拿，那么只有叫快递员等待一会儿
 *
 * 手上的事情还没有做完，等忙完了再去做其他的事情
 * @ClassName: SuspensionClient
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 16:40
 * History:
 * @<version> 1.0
 */
public class SuspensionClient {
    public static void main(String[] args) throws InterruptedException {
        final  RequestQueue queue = new RequestQueue();
        new ClientThread(queue, "Alex").start();
        ServerThread serverThread = new ServerThread(queue);
        serverThread.start();
//        serverThread.join();

        //如果这个地方休眠时间太少，基本上看不到 request参数为空的情况
        Thread.sleep(10000);
        serverThread.close();
    }
}
