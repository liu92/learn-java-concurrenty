package com.learn.concurrenty.chapter3;

/**
 * @ClassName: CreateThread
 * @Description: 构造Thread对象不知道的内容介绍
 * @Author: lin
 * @Date: 2020/3/20 12:36
 * History:
 * @<version> 1.0
 */
public class CreateThread {
    public static void main(String[] args) {
        Thread t1 = new Thread();
        t1.start();
        System.out.println(t1.getName());
    }
}
