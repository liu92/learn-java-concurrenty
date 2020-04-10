package com.learn.concurrenty.juc.untils.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: SemaphoreExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 9:30
 * History:
 * @<version> 1.0
 */
public class SemaphoreExample2 {
    public static void main(String[] args) {
         final  Semaphore semaphore = new Semaphore(1);
         int count =2;
        for (int i = 0; i <count ; i++) {
            new Thread(){
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName()+" in");
                        semaphore.acquire();
                        System.out.println(Thread.currentThread().getName()+" Get the #Semaphore");
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        semaphore.release();
                    }
                    System.out.println(Thread.currentThread().getName()+" out");
                }
            }.start();
        }
    }


}
