package com.learn.concurrenty.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: CompareAndSetLock
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 15:10
 * History:
 * @<version> 1.0
 */
public class CompareAndSetLock {
    private final AtomicInteger value = new AtomicInteger(0);

    private Thread lockedThread;

    public void tryLock() throws GetLockException {
        boolean success = value.compareAndSet(0, 1);
        if(!success){
            throw  new GetLockException("Get the Lock failed");
        }else{
            // 那个线程获得了锁，那么就是那个线程去释放
            lockedThread = Thread.currentThread();
        }
    }

    public void unLock(){
        //这个锁已经被释放了
        if(0 == value.get() || lockedThread ==null){
            return;
        }
        if(lockedThread == Thread.currentThread()){
            value.compareAndSet(1, 0);
        }

    }
}
