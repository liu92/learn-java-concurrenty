package com.learn.concurrent.design.chapter10;

import java.util.Random;

/**
 * ThreadLocal使用
 * @ClassName: ThreadLocalComplexTest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 17:27
 * History:
 * @<version> 1.0
 */
public class ThreadLocalComplexTest {

    /**
     * threadLocal
     */
    private static  ThreadLocal<String> threadLocal = new ThreadLocal<>();

    /**
     * 随机因子
     */
    private static final Random random = new Random(System.currentTimeMillis());


    /**
     * JVM start main thread
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            threadLocal.set("Thread-T1");
            try {
                Thread.sleep(random.nextInt(100));
                System.out.println(Thread.currentThread().getName() + " " + threadLocal.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        Thread t2 = new Thread(() -> {
            threadLocal.set("Thread-T2");
            try {
                Thread.sleep(random.nextInt(100));
                System.out.println(Thread.currentThread().getName() + " " + threadLocal.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("===================");
        System.out.println(Thread.currentThread().getName() + " " + threadLocal.get());

    }
}
