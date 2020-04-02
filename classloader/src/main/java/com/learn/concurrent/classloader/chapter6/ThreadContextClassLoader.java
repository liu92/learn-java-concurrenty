package com.learn.concurrent.classloader.chapter6;

import com.learn.concurrent.classloader.chapter3.MyClassLoader;

/**
 * 线程上下文以及数据库驱动
 *
 * @ClassName: ThreadContextClassLoader
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 11:16
 * History:
 * @<version> 1.0
 */
public class ThreadContextClassLoader {
    public static void main(String[] args) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(classLoader);
        Thread.currentThread().setContextClassLoader(new MyClassLoader());
        // 这种线程上下文，就相当于jdk开了一个 后门 去破坏父委托机制
        System.out.println(Thread.currentThread().getContextClassLoader());

    }
}
