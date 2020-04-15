package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * @ClassName: CompletableFutureExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/15 16:15
 * History:
 * @<version> 1.0
 */
public class CompletableFutureExample2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
       //supplyAsync();
       // hello 没有输出 是因为 线程是守护线程 , 所以使用join来等待期线程执行后
       //Thread.currentThread().join();

        //=====
        //Future<?> future = runAsync();
        //future.get();

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>"+anyOf().get());
        Thread.currentThread().join();
    }




    /**
     * {@link CompletableFuture#allOf(CompletableFuture[])}
     * @param
     * @return
     */
    private static  void allOf(){
         CompletableFuture.allOf(CompletableFuture.runAsync(()->{
                    try {
                        System.out.println("1======Start");
                        TimeUnit.SECONDS.sleep(5);
                        System.out.println("1=========end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).whenComplete((v, t) -> System.out.println("===============over===============")),
                CompletableFuture.supplyAsync(()->{
                    try {
                        System.out.println("2======Start");
                        TimeUnit.SECONDS.sleep(4);
                        System.out.println("2=========end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return  "Hello";
                }).whenComplete((v, t) -> System.out.println(v+ "===============over==============="))

        );
    }


    /**
     * {@link CompletableFuture#anyOf(CompletableFuture[])}
     * @param
     * @return
     */
    private static  Future<?> anyOf(){
         return CompletableFuture.anyOf(CompletableFuture.runAsync(()->{
            try {
                System.out.println("1======Start");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("1=========end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).whenComplete((v, t) -> System.out.println("===============over===============")),
          CompletableFuture.supplyAsync(()->{
              try {
                  System.out.println("2======Start");
                  TimeUnit.SECONDS.sleep(4);
                  System.out.println("2=========end");
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              return  "Hello";
          }).whenComplete((v, t) -> System.out.println(v+ "===============over==============="))

          );
    }


    /**
     * {@link CompletableFuture#completedFuture(Object)}
     * @param data
     * @return
     */
    private static  Future<Void> completed(String data){
        return  CompletableFuture.completedFuture(data).thenAccept(System.out::println);
    }


    /**
     * {@link CompletableFuture#supplyAsync(Supplier)}
     * @return
     */
    private static  Future<?> runAsync(){
      return  CompletableFuture.supplyAsync(Object::new)
            .thenAccept(o -> {
                try {
                    System.out.println("Obj======Start");
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("Obj========="+o);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).whenComplete((v, t) -> System.out.println("===============over==============="));
    }


    /**
     * {@link CompletableFuture#supplyAsync(Supplier)}
     */
    private static  void supplyAsync(){
        CompletableFuture.supplyAsync(Object::new)
                .thenAccept(o -> {
                    try {
                        System.out.println("Obj======Start");
                        TimeUnit.SECONDS.sleep(5);
                        System.out.println("Obj========="+o);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 输出完了之后 返回的是void类型
                }).runAfterBoth(CompletableFuture.supplyAsync(()->"Hello")
                .thenAccept(s->{
                    try {
                        System.out.println("String======Start");
                        TimeUnit.SECONDS.sleep(3);
                        System.out.println("String========="+s);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }),()->System.out.print("=======finished======")
        );
    }















}
