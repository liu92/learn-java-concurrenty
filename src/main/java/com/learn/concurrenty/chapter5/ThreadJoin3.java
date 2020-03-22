package com.learn.concurrenty.chapter5;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * 数据采集，一个服务器 一个线程
 *
 * @ClassName: ThreadJoin
 * @Description: Thread中join使用
 * @Author: lin
 * @Date: 2020/3/21 23:04
 * History:
 * @<version> 1.0
 */
public class ThreadJoin3 {
    public static void main(String[] args) throws InterruptedException {
        long startTimestamp = System.currentTimeMillis();

        Thread t1 = new Thread(new CaptureRunnable("M1", 10000L));
        Thread t2 = new Thread(new CaptureRunnable("M1", 10000L));
        Thread t3 = new Thread(new CaptureRunnable("M1", 10000L));


        t1.start();
        t2.start();
        t3.start();
        //如果不加使用 join,那么 就会出现 数据都还么有采集完，就报存了。
        // 所以 使用join 的目的就是 所有数据采集之后，再去保存
        t1.join();
        t2.join();
        t3.join();

        long endTimestamp = System.currentTimeMillis();
        System.out.printf("Save data begin timestamp is:%s, end timestamp is:%s\n", startTimestamp, endTimestamp);
        //如果不加入join，打印的结果是先打印
        // Save data begin timestamp.....
        // M1.....
        // M2....
        // M3

        //而使用了join，那么打印就是 这种
        // M1.....
        // M2....
        // M3
        //Save data begin timestamp.....
    }
}

class CaptureRunnable implements Runnable{

    private String machineName;

    private long spendTime;

    public CaptureRunnable(String machineName, long spendTime) {
        this.machineName = machineName;
        this.spendTime = spendTime;
    }

    /**
     */
    @Override
    public void run() {
        try {
            Thread.sleep(spendTime);
            System.out.printf(machineName  + "completed data capture at timestamp [%s] and successfully.\n", spendTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public  String getResult(){
        return  machineName + "finish.";
    }
}
