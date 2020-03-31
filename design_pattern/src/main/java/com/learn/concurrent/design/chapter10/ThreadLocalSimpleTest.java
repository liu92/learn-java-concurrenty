package com.learn.concurrent.design.chapter10;

/**
 * ThreadLocal使用
 * @ClassName: ThreadLocalSimpleTest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 17:27
 * History:
 * @<version> 1.0
 */
public class ThreadLocalSimpleTest {

    private static  ThreadLocal<String> threadLocal = new ThreadLocal();

    /**
     * JVM start main thread
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        threadLocal.set("Alex");
        Thread.sleep(1000);
        String value = threadLocal.get();
        System.out.println(value);
    }
}
