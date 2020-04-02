package com.learn.concurrenty.juc.atomic;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName: AtomicBooleanFlag
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 16:06
 * History:
 * @<version> 1.0
 */
public class AtomicBooleanFlag {
    /**
     * 这个可以替代 volatile 和 boolean 作为一个flag的标志
     */
    private final  static AtomicBoolean flag = new AtomicBoolean(true);
    public static void main(String[] args) throws InterruptedException {
        new Thread(){
            @Override
            public void run() {
                while (flag.get()){
                    try {
                        Thread.sleep(1000);
                        System.out.println("I am working");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("I am finished.");
            }
        }.start();

        Thread.sleep(5000);
        flag.set(false);
    }
}
