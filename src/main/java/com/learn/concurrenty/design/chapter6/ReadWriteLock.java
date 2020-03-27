package com.learn.concurrenty.design.chapter6;

/**
 * @ClassName: ReadWriteLock
 * @Description: 读写锁分离设计
 * @Author: lin
 * @Date: 2020/3/27 9:53
 * History:
 * @<version> 1.0
 */
public class ReadWriteLock {
    /**
     * 读线程多少个
     */
    private int readingReaders = 0;
    /**
     * 等待读 的线程 多少个
     */
    private int waitingReaders = 0;
    /**
     * 写线程 多少个
     */
    private int writingWriters = 0;
    /**
     * 等待写 的线程 多少个
     */
    private int waitingWriters = 0;

    /**
     * 这里 设置更偏向于 Writer
     */
    private boolean preferWriter = true;

    public ReadWriteLock(){
        this(true);
    }

    public ReadWriteLock(boolean preferWriter){
        this.preferWriter = preferWriter;
    }

    /**
     * 读 锁
     */
    public synchronized  void readLock()throws  InterruptedException{
        // 等待读的线程++
        this.waitingReaders++;
        try {
            // 判断，如果写线程 大于0， 那么就等待
            // 在读取的时候 如果更偏向于 writer那么 就是
            // 添加判断,如个 读的时候 preferWriter =true 并且 等待的 大于0
            // 那么就进行等待 ，这样在使用的时候就会更公平些，writer些的时候就会和read 一样
            while (writingWriters > 0 || (preferWriter && waitingWriters > 0)) {
                this.wait();
            }
            // 如果没有，那么读线程就 ++
            this.readingReaders ++;
        } finally {
            // 等待读的线程 减减
            this.waitingReaders --;
        }
    }

    /**
     * 释放 读 锁
     */
    public synchronized  void readUnLock(){
        //释放锁
        this.readingReaders--;
        this.notifyAll();
    }

    /**
     *  写 锁
     */
    public synchronized void writeLock() throws InterruptedException {
        // 写等待线程 ++
       this.waitingWriters ++;
       try {
           // 当有读的 或者 写的 大于 零，那么就不能写， 让其等待
           while (readingReaders > 0 || writingWriters > 0){
               this.wait();
           }
           this.writingWriters++;
       }finally {
           // 写等待线程 --
           this.waitingWriters--;
       }
    }

    public synchronized void writeUnLock(){
        this.writingWriters --;
        this.notifyAll();
    }
}

