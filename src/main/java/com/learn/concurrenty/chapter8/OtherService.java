package com.learn.concurrenty.chapter8;

/**
 * 其他 的服务
 * @ClassName: OtherService
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 15:27
 * History:
 * @<version> 1.0
 */
public class OtherService {
    private final Object LOCK = new Object();

    private DeadLock  deadLock;

    public void s1(){
        synchronized (LOCK){
            System.out.println("s1=============");
        }
    }

    public void s2(){
        synchronized (LOCK){
            System.out.println("s2=============");
            deadLock.m2();
        }
    }



    public void setDeadLock(DeadLock deadLock) {
        this.deadLock = deadLock;
    }
}
