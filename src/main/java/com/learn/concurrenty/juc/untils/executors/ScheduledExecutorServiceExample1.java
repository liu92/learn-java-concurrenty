package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName: ScheduledExecutorServiceExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/15 13:41
 * History:
 * @<version> 1.0
 */
public class ScheduledExecutorServiceExample1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

        /*ScheduledFuture<?> future = executor.schedule(()->
                System.out.println("i will be invoked!"), 2 , TimeUnit.SECONDS);
        // cancel之后还会执行吗？ cancel 之后就不会再执行了
        System.out.println(future.cancel(true));*/

       /* ScheduledFuture<Integer> scheduled =executor.schedule(()->2, 2, TimeUnit.SECONDS);
        System.out.println(scheduled.get());*/

        /**
         *period 2 seconds execute a task.
         * but the task spend 5 seconds actually
         *
         * solution 1: (crontab/quartz/Control-M)
         *
         * period first policy
         * 0:5 (1个任务在执行的过程中实际上花了5秒)
         * 2:5 (周期2s到了之后，下一个 有去启动了一个)
         * 4:5 (在第4秒时候又会去启动一个)
         *
         * solution 2:(JDK timer)
         * (周期还是2)
         * 0:5(第零秒执行了一个任务 花了5秒，等这个执行结束之后，不会去重启 一个
         * 去检查 这个执行了5秒 超过了这个周期2秒，所以从5秒开始有执行了一个)
         * 5:5
         * 10:5
         *
         * 下面的测试休眠5秒，看看这个是和那个相同
         */
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
        }, 1,2, TimeUnit.SECONDS);

    }
}
