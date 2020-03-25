package com.learn.concurrenty.chapter12;

import java.util.Arrays;

/**
 * @ClassName: ThreadGroupApi
 * @Description: ThreadGroupApi 一些使用
 * @Author: lin
 * @Date: 2020/3/24 17:30
 * History:
 * @<version> 1.0
 */
public class ThreadGroupApi {
    public static void main(String[] args) {
        ThreadGroup tg1 = new ThreadGroup("TGroup1");
        Thread t1 = new Thread(tg1, "t1") {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        // 这里取打断 ,就退出
                        break;
                        // 那么t2 也会
                    }
                }
            }
        };
//        tg1.setDaemon(true);
        t1.start();


        //这个入 没有父类，就不会被打断
        ThreadGroup tg2 = new ThreadGroup(tg1, "TGroup2");
        Thread t2 = new Thread(tg2, "T2") {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //中断 ，也退出
                        break;
                    }
                }
            }
        };
        t2.start();

        // activeCount 评估活跃的线程
        System.out.println(tg1.activeCount());
        System.out.println(tg1.activeGroupCount());
        //查看是否有权限修改
        t2.checkAccess();


        System.out.println("===========================");
        Thread[] ts1 = new Thread[tg1.activeCount()];
        tg1.enumerate(ts1);
        Arrays.asList(ts1).forEach(System.out::println);

        System.out.println("===========================");
        tg1.enumerate(ts1, true);
        Arrays.asList(ts1).forEach(System.out::println);


        System.out.println("===========================");
        ts1 = new Thread[10];
        Thread.currentThread().getThreadGroup().enumerate(ts1, true);
        Arrays.asList(ts1).forEach(System.out::println);

        //这里取打断
        tg1.interrupt();
    }
}
