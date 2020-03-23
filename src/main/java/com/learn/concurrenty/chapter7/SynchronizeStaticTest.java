package com.learn.concurrenty.chapter7;

/**
 * @ClassName: SynchronizeStaticTest
 * @Description: 测试 同步静态方法
 * @Author: lin
 * @Date: 2020/3/23 15:07
 * History:
 * @<version> 1.0
 */
public class SynchronizeStaticTest {
    public static void main(String[] args) {
        Thread t1 = new Thread("T1"){
            @Override
            public void run() {
                SynchronizeStatic.method1();
            }
        };
        t1.start();


        Thread t2 = new Thread("T2"){
            @Override
            public void run() {
                SynchronizeStatic.method2();
            }
        };
        t2.start();

        //两个线程去调用 同步静态方法，如果这个两个同步静态的锁补一样那么就会同时输出
        // 如果两个方的锁是同一个，那么这个锁被两个线程争抢，
        // 谁先抢到那么就执行，另外的一个线程就需要等待 锁的持有线程是否锁，才能获取到



        Thread t3 = new Thread("T3"){
            @Override
            public void run() {
                SynchronizeStatic.method3();
            }
        };
        t2.start();
    }
}
