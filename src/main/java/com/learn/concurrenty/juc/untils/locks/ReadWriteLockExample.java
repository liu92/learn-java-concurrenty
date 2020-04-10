package com.learn.concurrenty.juc.untils.locks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 读写锁
 * @ClassName: ReadWriteLockExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 14:12
 * History:
 * @<version> 1.0
 */
public class ReadWriteLockExample {
    private final static ReentrantLock LOCK = new ReentrantLock(true);
    private final static List<Long> data = new ArrayList<>();

    /**
     * Write Write  x
     * Write Read   x
     * Read  Write  x
     * Read  Read   ok
     *
     * 读写分离锁
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(ReadWriteLockExample::write);
        t1.start();
        TimeUnit.SECONDS.sleep(1);

        Thread t2 = new Thread(ReadWriteLockExample::read);
        t2.start();
        TimeUnit.SECONDS.sleep(1);
    }

    public static void  write(){
        try {
            LOCK.lock();
            data.add(System.currentTimeMillis());
            //为了模拟写数据比较耗时，那么就让其睡 几秒
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    public static void  read(){
        try {
            LOCK.lock();
            data.forEach(System.out::println);
            //为了模拟写数据比较耗时，那么就让其睡 几秒
            // 同样读数据也是一样，让其睡几秒
            TimeUnit.SECONDS.sleep(5);
            System.out.println(Thread.currentThread().getName() + "============");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }







}
