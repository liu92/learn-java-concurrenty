package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.*;

/**
 * @ClassName: FutureExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/14 14:35
 * History:
 * @<version> 1.0
 */
public class FutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        /*testGet();*/
        testGetWithTimeOut();
    }

    /**
     * {@link Future#get()}
     */
    private static  void testGet() throws ExecutionException, InterruptedException {
        ExecutorService executorService =  Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(()->{
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  10;
        });
        //===========不用去等待， 快速返回有机会去执行其他的操作=====================
        System.out.println("==============i will be printed quickly.===================");
        //================================

        Thread callerThread = Thread.currentThread();
        new Thread(()->{
            // 这个线程启动了，但是还没有具备可以执行的running状态 会有可能先执行下面的代码
            // 所以这个让其休眠几秒，让其线程到达running状态
            try {
                TimeUnit.MILLISECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 让这个线程去打断看看能不能接着往下执行
            callerThread.interrupt();
        }).start();
        //  因为这个在拿取结果的时候需要等待10s
        // 在等的过程中 是无法打断的，所以这个get 方法 打断的是main
        Integer result = future.get();
        System.out.println("=====================" + result + "========================");
    }



    /**
     * {@link Future#get(long, TimeUnit)}
     */
    private static  void testGetWithTimeOut() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService executorService =  Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(()->{
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("=====================");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  10;
        });

        Integer result = future.get(5, TimeUnit.SECONDS);
            //超时了之后 也会去执行这个
        System.out.println(result);

    }
}
