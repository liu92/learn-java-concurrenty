package com.learn.concurrenty.juc.untils.threadpool;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @ClassName: ThreadPoolExecutorTask
 * @Description:
 * @Author: lin
 * @Date: 2020/4/12 22:30
 * History:
 * @<version> 1.0
 */
public class ThreadPoolExecutorTask {
    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(10, 20, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), r->{
            Thread t = new Thread(r);
            return t;
        }, new ThreadPoolExecutor.AbortPolicy());

        IntStream.range(0, 20).boxed().forEach(i -> executorService.execute(()->{
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println(Thread.currentThread().getName() + " [" + i + "] finish done.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        List<Runnable> runnableList = null;
        try {
            runnableList = executorService.shutdownNow();
            System.out.println("===================over======================");
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("======================over==========================");
        System.out.println(runnableList);
        System.out.println(runnableList.size());

        /**
         * shutdown
         * 20 threads
         *    10 threads work
         *    10 idle(空闲)
         *
         * showdown invoked
         * 1. 10 waiting to finished the work
         * 2. 10 interrupt the idle works
         * 3. 20 idle threads will exist
         *
         * shutdownNow
         * 10  thread queue elements 10
         * 10  running
         * 10  stored in the blocking queue
         *
         * 1.return list<Runnable> remain 10 un handle runnable
         *
         * 2.interrupted all of threads in the pool
         */
    }


}
