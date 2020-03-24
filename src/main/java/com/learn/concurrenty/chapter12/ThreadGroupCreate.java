package com.learn.concurrenty.chapter12;

import java.util.Arrays;

/**
 * @ClassName: ThreadGroupCreate
 * @Description: Thread能访问ThreadGroup的那些方法
 * @Author: lin
 * @Date: 2020/3/24 16:55
 * History:
 * @<version> 1.0
 */
public class ThreadGroupCreate {

    public static void main(String[] args) {
        ThreadGroup threadGroup = new ThreadGroup("TGroup1");
        Thread t1 = new Thread(threadGroup, "t1"){
            @Override
            public void run() {
               while (true){
                   try {
//                       System.out.println(getThreadGroup().getName());
//                       System.out.println(getThreadGroup().getParent());
                       // sleep不会释放锁
                       Thread.sleep(10_000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            }
        };
        t1.start();


        ThreadGroup threadGroup2 = new ThreadGroup("TGroup2");
        Thread  t2 = new Thread(threadGroup2, "T2"){
            @Override
            public void run() {
                System.out.println("========="+threadGroup.getName());
                Thread[] threads = new Thread[threadGroup.activeCount()];
                threadGroup.enumerate(threads);

                Arrays.asList(threads).forEach(System.out::println);
            }
        };
        t2.start();


        /**
         *  sleep是线程方法，wait是object方法；看区别，主要是看CPU的运行机制：
         *  它们的区别主要考虑两点：1.cpu是否继续执行、2.锁是否释放掉。
         *
         * 对于这两点，首先解释下cpu是否继续执行的含义：cpu为每个线程划分时间片去执行，
         * 每个时间片时间都很短，cpu不停地切换不同的线程，
         * 以看似他们好像同时执行的效果。
         *
         * 其次解释下锁是否释放的含义：锁如果被占用，
         * 那么这个执行代码片段是同步执行的，如果锁释放掉，
         * 就允许其它的线程继续执行此代码块了。
         *
         * sleep ，释放cpu资源，不释放锁资源，如果线程进入sleep的话，
         * 释放cpu资源，如果外层包有Synchronize，那么此锁并没有释放掉。
         *
         * wait，释放cpu资源，也释放锁资源，一般用于锁机制中 肯定是要释放掉锁的，
         * 因为notify并不会立即调起此线程，因此cpu是不会为其分配时间片的，
         * 也就是说wait 线程进入等待池，cpu不分时间片给它，锁释放掉。
         *
         * sleep：Thread类的方法，必须带一个时间参数。
         * 会让当前线程休眠进入阻塞状态并释放CPU（阿里面试题 Sleep释放CPU，
         * wait 也会释放cpu，因为cpu资源太宝贵了，只有在线程running的时候，
         * 才会获取cpu片段），提供其他线程运行的机会且不考虑优先级，
         * 但如果有同步锁则sleep不会释放锁即其他线程无法获得同步锁
         * 可通过调用interrupt()方法来唤醒休眠线程。
         */

    }
}
