package com.learn.concurrenty.juc.untils.executors;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @ClassName: CompletableFutureExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/15 16:15
 * History:
 * @<version> 1.0
 */
public class CompletableFutureExample1 {
    public static void main(String[] args) throws InterruptedException {

        // 因为这里写在了main方法里， 并且CompletableFuture
        // 里面会将其设置为守护线程.
       /* CompletableFuture.runAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 执行完返回结果
            // 注册一个callback
        }).whenComplete((v, t) -> System.out.println("DONE"));
        System.out.println("=========i am not blocked=============");*/



        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 一个阶段 一个阶段的执行
        // 定义了一组callable
       /* List<Callable<Integer>> tasks = IntStream.range(0, 10).boxed()
                .map(i -> (Callable<Integer>) () -> get()).collect(Collectors.toList());
        //executorService 提交了一批的 callable 然后将结果拿出来
        executorService.invokeAll(tasks).stream().map(future ->{
            try {
                // future 分别将值提取出来
               return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw  new RuntimeException(e);
            }
        }).parallel().forEach(CompletableFutureExample1::display);*/


        /**
         * 使用CompletableFuture
         * CompletableFuture 是future和 ExecutorService的结合
         *
         */
        IntStream.range(0, 10).boxed()
                // 执行是交替执行, 不想上面的一批执行完后再执行下一批
                // 并行的执行
                .forEach(i -> CompletableFuture.supplyAsync(CompletableFutureExample1::get)
                .thenAccept(CompletableFutureExample1::display)
                //执行结束之后
                .whenComplete((v, t) -> System.out.println(i + " DONE")));
        Thread.currentThread().join();
    }

    private static  void display(int data ){
        int value = ThreadLocalRandom.current().nextInt(20);
        try {
            System.out.println(Thread.currentThread().getName() + " display will be sleep " + value);
            TimeUnit.SECONDS.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() +
                " display execute done " + data);
    }



    private static  int get(){
        int value = ThreadLocalRandom.current().nextInt(20);
        try {
            System.out.println(Thread.currentThread().getName() + " will be sleep " + value);
            TimeUnit.SECONDS.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " execute done " + value);
        return  value;
    }















}
