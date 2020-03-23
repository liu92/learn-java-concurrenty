package com.learn.concurrenty.chapter8;

/**
 * 死锁
 * @ClassName: DeadLock
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 15:26
 * History:
 * @<version> 1.0
 */
public class DeadLock {
    private OtherService service ;

    public DeadLock(OtherService otherService){
        this.service = otherService;
    }
    private final Object lock = new Object();


    public void m1(){
        synchronized (lock){
            System.out.println("m1");
            service.s1();
        }
    }

    public void m2(){
        synchronized (lock){
            System.out.println("m2");
        }
    }
}
