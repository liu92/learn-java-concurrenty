package com.learn.concurrenty.juc.untils.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: SemaphoreExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 9:30
 * History:
 * @<version> 1.0
 */
public class SemaphoreExample {
    public static void main(String[] args) {
         final  SemaphoreLock lock = new SemaphoreLock();
         new Thread(){
             @Override
             public void run() {
                 try {
                     System.out.println(Thread.currentThread().getName()+" is running");
                     lock.lock();
                     System.out.println(Thread.currentThread().getName()+" get the #SemaphoreLock");
                     TimeUnit.SECONDS.sleep(10);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }finally {
                     lock.unlock();
                 }
                 System.out.println(Thread.currentThread().getName()+" Released #SemaphoreLock");
             }
         }.start();
    }

    static class  SemaphoreLock{
        private final Semaphore semaphore = new Semaphore(1);

        public  void lock() throws InterruptedException {
            //申请一个许可证
            semaphore.acquire();
        }

        public void unlock(){
            semaphore.release();
        }
    }








}
