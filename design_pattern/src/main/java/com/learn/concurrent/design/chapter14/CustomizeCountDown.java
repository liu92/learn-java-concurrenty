package com.learn.concurrenty.design.chapter14;

/**
 * 参照jdk CountDownLatch 自定义实现
 * @ClassName: CustomizeCountDown
 * @Description: CustomizeCountDown
 * @Author: lin
 * @Date: 2020/3/28 21:53
 * History:
 * @<version> 1.0
 */
public class CustomizeCountDown {
    private final int total;
    private int counter = 0;

    public CustomizeCountDown(int total){
        this.total = total;
    }

    public  void down(){
        synchronized (this){
            this.counter ++;
            this.notify();
        }
    }

    public  void await() throws InterruptedException {
        synchronized (this){
            while (counter != total){
                this.wait();
            }
        }
    }
}
