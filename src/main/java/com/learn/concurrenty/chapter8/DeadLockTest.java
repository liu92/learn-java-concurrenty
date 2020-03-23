package com.learn.concurrenty.chapter8;

import com.learn.concurrenty.chapter7.SynchronizeStatic;

/**
 * 死锁测试
 * @ClassName: DeadLock
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 15:26
 * History:
 * @<version> 1.0
 */
public class DeadLockTest {
    public static void main(String[] args) {
        OtherService otherService = new OtherService();
        DeadLock deadLock = new DeadLock(otherService);
        otherService.setDeadLock(deadLock);

        Thread t1 = new Thread(){
            @Override
            public void run() {
                while (true){
                    deadLock.m1();
                }
            }
        };
        t1.start();

        Thread t2= new Thread(){
            @Override
            public void run() {
                while (true) {
                    otherService.s2();
                }
            }
        };
        t2.start();

    }
}
