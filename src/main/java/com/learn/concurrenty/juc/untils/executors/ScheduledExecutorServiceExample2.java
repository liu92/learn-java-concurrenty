package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName: ScheduledExecutorServiceExample2
 * @Description:
 * @Author: lin
 * @Date: 2020/4/15 13:41
 * History:
 * @<version> 1.0
 */
public class ScheduledExecutorServiceExample2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        testScheduleWithFixedDelay();
    }

    private static  void  testScheduleWithFixedDelay(){
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        final AtomicLong interval = new AtomicLong(0L);
        //设置一个scheduled 它会固定的延迟
        ScheduledFuture<?> future = executor.scheduleWithFixedDelay(() ->
        {
            long currentTimeMills = System.currentTimeMillis();
            if(interval.get() == 0){
                System.out.printf("The first time trigger task at %d\n", currentTimeMills);
            }else {
                System.out.printf("The actually spend [%d]\n", currentTimeMills - interval.get());
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            interval.set(currentTimeMills);
            System.out.println(Thread.currentThread().getName());
        }, 1, 2, TimeUnit.SECONDS);

    }

    private static  void test1() throws InterruptedException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        // 这个值默认是false,就是在shutdown后 不在周期性的去执行
        System.out.println(executor.getContinueExistingPeriodicTasksAfterShutdownPolicy());
        final AtomicLong interval = new AtomicLong(0L);
        ScheduledFuture<?> future =executor.scheduleAtFixedRate(()->
        {
            long currentTimeMills = System.currentTimeMillis();
            if(interval.get() == 0){
                System.out.printf("The first time trigger task at %d\n", currentTimeMills);
            }else {
                System.out.printf("The actually spend [%d]\n", currentTimeMills - interval.get());
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            interval.set(currentTimeMills);
            // period 是固定的
        }, 1,2, TimeUnit.SECONDS);

        // 让其真正执行了
        TimeUnit.MILLISECONDS.sleep(1200);
        executor.shutdown();
    }


    private static  void test2() throws InterruptedException{
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        System.out.println(executor.getContinueExistingPeriodicTasksAfterShutdownPolicy());
        final AtomicLong interval = new AtomicLong(0L);
        //设置一个scheduled 它会固定的延迟
        ScheduledFuture<?> future = executor.scheduleWithFixedDelay(() ->
        {
            long currentTimeMills = System.currentTimeMillis();
            if(interval.get() == 0){
                System.out.printf("The first time trigger task at %d\n", currentTimeMills);
            }else {
                System.out.printf("The actually spend [%d]\n", currentTimeMills - interval.get());
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            interval.set(currentTimeMills);
            System.out.println(Thread.currentThread().getName());
            // delay 不是固定的
        }, 1, 2, TimeUnit.SECONDS);
    }
}
