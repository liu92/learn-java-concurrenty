package com.learn.concurrenty.juc.untils.locks;


import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: ReentrantLockExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 11:12
 * History:
 * @<version> 1.0
 */
public class ReentrantLockExample {
    private static final ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) throws InterruptedException {
//        IntStream.range(0, 2).forEach(i ->
//                new Thread(ReentrantLockExample::needLock).start());

//        Thread t1 = new Thread(ReentrantLockExample::testUnlockInterrupt);
//        t1.start();
//        TimeUnit.SECONDS.sleep(1);
//
//        Thread t2 = new Thread(ReentrantLockExample::testUnlockInterrupt);
//        t2.start();
//        TimeUnit.SECONDS.sleep(1);
//
//        t2.interrupt();
//        System.out.println("==================");

//        Thread t1 = new Thread(ReentrantLockExample::testTryLock);
//        t1.start();
//        TimeUnit.SECONDS.sleep(1);
//
//        Thread t2 = new Thread(ReentrantLockExample::testTryLock);
//        t2.start();

         Thread t1 = new Thread(ReentrantLockExample::testUnlockInterrupt);
         t1.start();
         TimeUnit.SECONDS.sleep(1);

         Thread t2 = new Thread(ReentrantLockExample::testUnlockInterrupt);
         t2.start();
         TimeUnit.SECONDS.sleep(1);
         Optional.of(lock.getQueueLength()).ifPresent(System.out::println);
         Optional.of(lock.hasQueuedThreads()).ifPresent(System.out::println);
         //这个方法表示 那个线程被放到了 阻塞队列中去
         Optional.of(lock.hasQueuedThread(t2)).ifPresent(System.out::println);
         Optional.of(lock.hasQueuedThread(t1)).ifPresent(System.out::println);
    }


    public static void testTryLock(){
        //尝试获取锁，如果没有获取到锁，那么就退出锁的竞争了
        if(lock.tryLock()){
            try {
                Optional.of("The thread-" + Thread.currentThread().getName()
                        + " get lock and will do working")
                        .ifPresent(System.out::println);
                while (true){}
            }finally {
                lock.unlock();
            }
        }
        else {
            Optional.of("The thread-" + Thread.currentThread().getName()
                    + " not get lock. ")
                    .ifPresent(System.out::println);
        }

    }


    public static void testUnlockInterrupt(){
        try {
            // 抢锁，如果抢不到锁那么其他线程可以被其他线程打断。
            lock.lockInterruptibly();
            Optional.of(Thread.currentThread().getName() + ":" + lock.getHoldCount())
            .ifPresent(System.out::println);
            Optional.of("The thread-" + Thread.currentThread().getName()
                    + " get lock and will do working")
                    .ifPresent(System.out::println);
           while (true){
           }
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }


    public  static  void needLock(){
        lock.lock();
        try {
            Optional.of("The thread-" + Thread.currentThread().getName() + " get lock and will do working")
            .ifPresent(System.out::println);
            TimeUnit.SECONDS.sleep(10);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public static void  needLockBySync(){
        synchronized (ReentrantLockExample.class){
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
