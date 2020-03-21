package com.learn.concurrenty.chapter3;

import java.util.Arrays;

/**
 * @ClassName: CreateThread
 * @Description: 构造Thread对象不知道的内容介绍
 * @Author: lin
 * @Date: 2020/3/20 12:36
 * History:
 * @<version> 1.0
 */
public class CreateThread2 {
    public static void main(String[] args) {
        Thread t1 = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
        //如果不传入ThreadGroup那么就会去取 它父类的ThreadGroup()
//        System.out.println(t1.getThreadGroup());
//        System.out.println(Thread.currentThread().getName());

        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
//        System.out.println(threadGroup.getName());
        System.out.println(threadGroup.activeCount());

        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);
//        for (Thread thread : threads) {
//            System.out.println(thread);
//        }
        Arrays.asList(threads).forEach(System.out::println);
    }
}
