package com.learn.concurrenty.design.chapter6;

/**
 * @ClassName: ReadWorker
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 10:35
 * History:
 * @<version> 1.0
 */
public class ReadWorker extends Thread {
    private final  ShareData data;

    public  ReadWorker(ShareData data){
        this.data = data;
    }

    @Override
    public void run() {
        try {
            while (true) {
                char[] readBuffer = data.read();
                System.out.println(Thread.currentThread().getName()
                        + " reads " + String.valueOf(readBuffer));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
