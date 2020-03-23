package com.learn.concurrenty.chapter7;

/**
 * 同步静态方法
 * @ClassName: SynchronizeStatic
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 15:04
 * History:
 * @<version> 1.0
 */
public class SynchronizeStatic {
    public synchronized static void method1(){
        System.out.println("m1 " + Thread.currentThread().getName());
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void method2(){
        System.out.println("m2 " + Thread.currentThread().getName());
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public  static void method3(){
        System.out.println("m3 " + Thread.currentThread().getName());
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
