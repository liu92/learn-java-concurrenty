package com.learn.concurrent.design.chapter9;

import java.util.Random;

/**
 * @ClassName: ClientThread
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 16:19
 * History:
 * @<version> 1.0
 */
public class ClientThread extends Thread {
    private final RequestQueue queue;

    private final Random random;

    private final  String sendValue;

    public  ClientThread(RequestQueue queue, String sendValue){
        this.queue = queue;
        this.sendValue = sendValue;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        int t =10;
        for (int i = 0; i < t; i++) {
            System.out.println("Client -> request " + sendValue);
            queue.putRequest(new Request(sendValue));
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
