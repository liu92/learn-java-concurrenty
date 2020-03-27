package com.learn.concurrenty.design.chapter9;

import java.util.Random;

/**
 * @ClassName: ServerThread
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 16:23
 * History:
 * @<version> 1.0
 */
public class ServerThread extends  Thread {
    private final RequestQueue queue;

    private final Random random;

    private volatile  boolean close = false;

    ServerThread(RequestQueue queue){
        this.queue = queue;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        while (!close){
            //1、当flag = false 时，有可能在getRequest 已经wait住了 ，
            // 是判断不到这个值的,
            Request request = queue.getRequest();
            if(null == request){
                ///2、sleep的过程中，进行了打断，那么catch中收到了中断信号
                // 这个时候就退出出去了
                System.out.println("Received the empty request");
                continue;
            }
            System.out.println("server -> " + request.getValue());
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                return;
            }
            // 3、本身在休眠的过程中  然后让其中断
        }
    }

    public  void close(){
        this.close = true;
        this.interrupt();
    }
}
