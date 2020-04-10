package com.learn.concurrenty.juc.untils.locks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 * @ClassName: ReadWriteLockExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 14:12
 * History:
 * @<version> 1.0
 */
public class ReadWriteLockExample1 {
    private final static ReentrantReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock(true);
    private final static Lock READ_LOCK = READ_WRITE_LOCK.readLock();
    private final static Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();
    private final static List<Long> data = new ArrayList<>();

    /**
     * Write Write  x
     * Write Read   x
     * Read  Write  x
     * Read  Read   ok
     *
     * @param args
     */
    public static void main(String[] args) {

    }

    public static void  write(){
        try {
            WRITE_LOCK.lock();
            data.add(System.currentTimeMillis());
            //为了模拟写数据比较耗时，那么就让其睡 几秒
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    public static void  read(){
        try {
            READ_LOCK.lock();
            data.forEach(System.out::println);
            //为了模拟写数据比较耗时，那么就让其睡 几秒
            // 同样读数据也是一样，让其睡几秒
            TimeUnit.SECONDS.sleep(5);
            System.out.println(Thread.currentThread().getName() + "============");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            READ_LOCK.unlock();
        }
    }







}
