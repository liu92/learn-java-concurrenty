package com.learn.concurrent.design.chapter17;

import java.util.Random;

/**
 * Worker-Thread Design pattern
 * @ClassName: WorkerThread
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 9:11
 * History:
 * @<version> 1.0
 */
public class WorkerThread extends  Thread{
    private  final  Channel channel;
    private static  final Random RANDOM = new Random(System.currentTimeMillis());

    public WorkerThread(String name, Channel channel) {
        super(name);
        this.channel =channel;
    }

    @Override
    public void run() {
        while (true){
            //工人 拿东西
            channel.take().execute();
            try {
                Thread.sleep(RANDOM.nextInt(1_000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
