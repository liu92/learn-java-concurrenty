package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @ClassName: ExecutorServiceExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/13 14:01
 * {@link java.util.concurrent.ExecutorService}
 * History:
 * @<version> 1.0
 */
public class ExecutorServiceExample {
    public static void main(String[] args) throws InterruptedException {
//      isShutDown();
//      isTerminated();
//      executeRunnableError();
        executeRunnableTask();
    }

    /**
     * Question:
     * when invoked the shutdown method, can execute the new runnable?
     * Answer:
     * No !!! the executor service will rejected after shutdown.
     */
    private static  void isShutDown(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(executorService.isShutdown());
        // 前面的任务还没有执行完，下面的打印不会打印出来
        // 如果注释掉  executorService.shutdown(), 那么就可以看到下面的可以打印出来了
        // 这个是因为上面任务已经执行完了
        executorService.shutdown();
        System.out.println(executorService.isShutdown());
        executorService.execute(()-> System.out.println("i will be execute after shutdown????"));
    }

    /**
     *
     */
    private static void isTerminated(){
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
        System.out.println(executorService.isShutdown());
        // 这个executor 还没有真正的总结掉，所以这是false
        System.out.println(executorService.isTerminated());
        // 是否在终结的过程中
        System.out.println(((ThreadPoolExecutor)executorService).isTerminating());
    }



    private static void executeRunnableError() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10, new MyThreadFactory());
        executorService.execute(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        IntStream.range(0, 10).boxed().forEach(i ->
                executorService.execute(()-> System.out.println(1/0)));
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println("================================");
    }

    private static class MyThreadFactory implements ThreadFactory{
        private final static AtomicInteger SEQ = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("My-Thread-" + SEQ.getAndIncrement());
            thread.setUncaughtExceptionHandler((t,cause) ->{
                System.out.println("The thread " + t.getName() + " execute failed.");
                cause.printStackTrace();
                System.out.println("================================");
            });
            return thread   ;
        }
    }


    /**
     *
     *                                        |----->
     *                                        |----->
     * send request-----> store db ----10---> |----->
     *                                        |----->
     *                                        |----->
     */
    private static void executeRunnableTask() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10, new MyThreadFactory());
        IntStream.range(0, 10).boxed().forEach(i ->
                executorService.execute(
                        new MyTask(i) {
                            @Override
                            protected void error(Throwable cause) {
                                System.out.println("The no:" + i + " failed, update status to error");
                            }

                            @Override
                            protected void done() {
                                System.out.println("The no:" + i + " successful, update status to DONE");
                            }

                            @Override
                            protected void doExecute() {
                               if(i % 3 ==0){
                                   int tmp = i/0;
                               }
                            }

                            @Override
                            protected void doInit() {
                               //
                            }
                        }
                ));
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println("================================");
    }

    private abstract static class MyTask implements Runnable{
        protected final int no;
        private MyTask(int no){
            this.no = no;
        }

        @Override
        public void run() {
            try {
                this.doInit();
                this.doExecute();
                this.done();
            }catch (Throwable cause){
                this.error(cause);
            }
        }

        protected abstract void error(Throwable cause);

        protected abstract void done();

        protected abstract void doExecute();

        protected abstract void doInit();
    }
}
