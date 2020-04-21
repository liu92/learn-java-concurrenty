package com.learn.concurrenty.juc.untils.cyclibarrier;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch VS CyclicBarrier
 * 1、CountDownLatch不能rest, 而CyclicBarrier是可以循环使用的
 * 2、CountDownLatch工作线程之间相互不关心， CyclicBarrier工作线程必须等到同一个共同的点才去执行某个动作
 *
 * @ClassName: CyclicBarrierExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/9 22:18
 * History:
 * @<version> 1.0
 */
public class CyclicBarrierExample3 {


   static  class MyCountDownLatch extends CountDownLatch {
       private final  Runnable runnable;

       /**
        * Constructs a {@code CountDownLatch} initialized with the given count.
        *
        * @param count the number of times {@link #countDown} must be invoked
        *              before threads can pass through {@link #await}
        * @throws IllegalArgumentException if {@code count} is negative
        */
       public MyCountDownLatch(int count ,Runnable runnable) {
           super(count);
           this.runnable = runnable;
       }

       @Override
       public void countDown() {
           super.countDown();
           if(getCount() == 0){
               this.runnable.run();
           }
       }
   }

    public static void main(String[] args) {
        final  MyCountDownLatch myCountDownLatch = new MyCountDownLatch(2, new Runnable() {
            @Override
            public void run() {
                System.out.println("all of work finish done.");
            }
        });

        new Thread(){
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myCountDownLatch.countDown();
                System.out.println(Thread.currentThread()+getName() + " finished work");
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myCountDownLatch.countDown();
                System.out.println(Thread.currentThread()+getName() + " finished work");
            }
        }.start();

    }



}

