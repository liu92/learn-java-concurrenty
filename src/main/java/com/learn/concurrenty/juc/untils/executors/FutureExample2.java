package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName: FutureExample2
 * @Description:
 * @Author: lin
 * @Date: 2020/4/14 14:35
 * History:
 * @<version> 1.0
 */
public class FutureExample2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        /*testIsDone();*/
        testIsDone2();
    }

    /**
     * {@link Future#isDone()}
     */
    private static  void testIsDone() throws ExecutionException, InterruptedException {
        ExecutorService executorService =  Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(()->{
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  10;
        });
        Integer result = future.get();
        // 正常结束会返回
        // 还有一种是出现了错误
        System.out.println(result + " is done"+future.isDone());
    }


    /**
     * 如果出现了错误
     * {@link Future#isDone()}
     */
    private static  void testIsDone2() throws ExecutionException, InterruptedException {
        ExecutorService executorService =  Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(()->{
            // 还有一种是出现了错误
              throw  new RuntimeException();
        });
        // 这了要捕获异常，不然异常就抛出去了不能进行异常处理
        try {

            Integer result = future.get();
            System.out.println(result);
        }catch (Exception e){
            // 出现了问题 也表示结束了，但是不一定代表着正确的结束了
            System.out.println( " is done"+future.isDone());
        }
    }


    /**
     * try to cancel  maybe failed.
     * <ul>
     *  <li>task is completed already.</li>
     *  <li>has already been cancelled. </li>
     * </>
     *
     */
    private static void testCancel() throws ExecutionException, InterruptedException {
        ExecutorService executorService =  Executors.newCachedThreadPool();
        AtomicBoolean running = new AtomicBoolean(true);
        Future<Integer> future = executorService.submit(()-> {
//            try {
//                TimeUnit.SECONDS.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            while (running.get()){

            }
            return  10;
        });
        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println(future.cancel(true));
        // 已经被cancel过那么就不能再次被cancel.
//        System.out.println(future.cancel(true));

        System.out.println(future.isDone());
        //结果已经返回，但是任务 还一直在执行，这个任务有可能非常耗时，并且没有打断它的地方
        System.out.println(future.isCancelled());
    }



    /**
     * try to cancel  maybe failed.
     * <ul>
     *  <li>task is completed already.</li>
     *  <li>has already been cancelled. </li>
     * </>
     *
     */
    private static void testCancel2() throws ExecutionException, InterruptedException {
        ExecutorService executorService =  Executors.newCachedThreadPool();
        AtomicBoolean running = new AtomicBoolean(true);
        Future<Integer> future = executorService.submit(()-> {
//            try {
//                TimeUnit.SECONDS.sleep(10);
//                System.out.println("=========================");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            // 当遇到某个很长的任务，怎么去cancel喃，就是通过这种方式
            //!Thread.interrupted();
            while (!Thread.interrupted()){

            }
            System.out.println("11111111111111111111111");
            return  10;
        });
        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println(future.cancel(true));
        System.out.println(future.isDone());
        System.out.println(future.isCancelled());
        // 因为已经 cancel 所以再次去拿取，是拿取不到的,会抛出异常
//        System.out.println(future.get());
    }

}
