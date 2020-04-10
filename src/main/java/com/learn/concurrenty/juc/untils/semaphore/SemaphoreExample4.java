package com.learn.concurrenty.juc.untils.semaphore;

import java.util.Collection;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 *
 * @ClassName: SemaphoreExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 9:30
 * History:
 * @<version> 1.0
 */
public class SemaphoreExample4 {
    public static void main(String[] args) throws InterruptedException {
//        final  Semaphore semaphore = new Semaphore(5);
        final  MySemaphore semaphore = new MySemaphore(5);
        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    // 这个方法的意思 是获取完许可证
                    semaphore.drainPermits();
                    System.out.println(semaphore.availablePermits());
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release(5);
                }
                System.out.println("T1 finished");
            }
        };
        t1.start();

        TimeUnit.MICROSECONDS.sleep(1);


        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
                System.out.println("T2 finished");
            }
        };
        t2.start();

        TimeUnit.SECONDS.sleep(1);
        System.out.println("===" +semaphore.hasQueuedThreads());
        Collection<Thread> waitingThread = semaphore.getWaitingThreads();
        for (Thread t : waitingThread) {
            System.out.println("waitingThread========="+t);
        }
    }


    static class MySemaphore extends Semaphore{

        public MySemaphore(int permits) {
            super(permits);
        }

        public MySemaphore(int permits, boolean fair) {
            super(permits, fair);
        }

        public Collection<Thread> getWaitingThreads(){
            return super.getQueuedThreads();
        }
    }

}
