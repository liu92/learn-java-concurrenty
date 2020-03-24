package com.learn.concurrenty.chapter10;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * 实现自定义 锁
 * @ClassName: BooleanLock
 * @Description:
 * @Author: lin
 * @Date: 2020/3/24 11:18
 * History:
 * @<version> 1.0
 */
public class BooleanLock implements  CustomizeLock{
    /**
     * 初始值
     */
    private boolean initValue;
    private Collection<Thread> blockedThreadCollection = new ArrayList<>();

    /**
     * 这个 表示 那个线程，作用是那个线程加的锁，就是它自己去释放这个锁
     * 不能让其他的线程释放 加锁的这个线程
     */
    private Thread currentThread;

    /**
     * 在构造函数中去初始化
     */
    public BooleanLock() {
        this.initValue = false;
    }

    @Override
    public synchronized void lock() throws InterruptedException {
        // 如果这个锁 是true, 那么说明被一个线程抢到了，
        // 其他的线程等待，然后将这个线程加入到这个集合中去
        while (initValue){
            blockedThreadCollection.add(Thread.currentThread());
            this.wait();
        }
        blockedThreadCollection.remove(Thread.currentThread());
        // 如果这锁没有被 抢到那么 就设置
        this.initValue = true;
        currentThread = Thread.currentThread();
    }

    @Override
    public synchronized void lock(long mills) throws InterruptedException, TimeoutException {
          if(mills <= 0){
              lock();
          }
          long hasRemaining = mills;
          long endTime = System.currentTimeMillis() + mills;
          while (initValue){
              if(hasRemaining <= 0){
                  // 等待锁释放 超时
                  throw  new TimeoutException(" Time out");
              }
              //如果这个 锁被其他的线程抢到了 ，那么就将其他的线程加入到 等待队里中去
              blockedThreadCollection.add(Thread.currentThread());
              //让其他线程等待
              this.wait(mills);
              hasRemaining = endTime - System.currentTimeMillis();
          }
          //这里 一个线程抢到锁 将其值改为true
          this.initValue = true;
          this.currentThread = Thread.currentThread();
    }

    @Override
    public synchronized void unLock() {
        // 这个判断是 那个线程加的锁，就让那个线程自己释放这个锁，
        // 如果不判断 那么其他线程就可能来释放 这个被加锁的线程，
        if(Thread.currentThread() == currentThread) {
            // 这个锁是 initValue;
            this.initValue = false;
            Optional.of(Thread.currentThread().getName() + " release the lock monitor.")
                    .ifPresent(System.out::println);
            this.notifyAll();
        }
    }

    @Override
    public Collection<Thread> getBlockThread() {
        // 这里 可能存在修改 blockedThreadCollection, 所以这里不能被修改

        return Collections.unmodifiableCollection(blockedThreadCollection);
    }

    @Override
    public int getBLockSize() {
        return blockedThreadCollection.size();
    }
}
