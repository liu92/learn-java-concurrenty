package com.learn.concurrenty.chapter10;

import java.util.Collection;
import java.util.concurrent.TimeoutException;

/**
 * 自定义Lock锁
 * @ClassName: CustomizeLock
 * @Description:
 * @Author: lin
 * @Date: 2020/3/24 11:11
 * History:
 * @<version> 1.0
 */
public interface CustomizeLock {

    class  TimeOutException extends Exception{
        public  TimeOutException(String message){
            super(message);
        }
    }

    /**
     * 定义 锁方法， 允许中断 （synchronized 不允许中断）
     * @throws InterruptedException
     */
    void lock() throws InterruptedException;

    /**
     * 在规定的时间没有获取到锁
     * @param mills
     * @throws InterruptedException
     * @throws TimeoutException
     */
    void lock(long mills) throws  InterruptedException, TimeoutException;

    /**
     * 释放锁
     */
    void  unLock();

    /**
     * 获取阻塞住的线程
     * @return
     */
    Collection<Thread> getBlockThread();

    /**
     * 获取阻塞线程数量
     * @return
     */
    int getBLockSize();
}
