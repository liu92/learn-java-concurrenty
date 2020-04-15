package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.*;
import java.util.function.*;

/**
 * @ClassName: CompletableFutureExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/15 16:15
 * History:
 * @<version> 1.0
 */
public class CompletableFutureExample3 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

    }




    /**
     * {@link CompletableFuture#thenCompose(Function)}
     */
    private static  void thenCompose(){
        CompletableFuture.supplyAsync(()->{
            try {
                System.out.println("start the compose1");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("end the compose1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  "compose-1";
            // 上一个的输出作为下一个的输入
        }).thenCompose(s-> CompletableFuture.supplyAsync(()->{
            try {
                System.out.println("start the compose2");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("end the compose2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  s.length();
        }));

    }



    /**
     * {@link CompletableFuture#thenCombine(CompletionStage, BiFunction)}
     */
    private static  void thenCombine(){
        CompletableFuture.supplyAsync(()->{
            try {
                System.out.println("start the combine1");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("end the combine1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  "combine-1";
            // 组合
        }).thenCombine(CompletableFuture.supplyAsync(()->{
            try {
                System.out.println("start the combine2");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("end the combine2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  100;
        }),(s, i) -> s.length() > i).whenComplete((v, t) -> System.out.println(v));

    }



    /**
     * {@link CompletableFuture#acceptEither(CompletionStage, Consumer)}
     */
    private static  void acceptEither(){
        CompletableFuture.supplyAsync(()->{
            try {
                System.out.println("start the acceptEither1");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("end the acceptEither1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  "acceptEither-1";
            // 组合
        }).acceptEither(CompletableFuture.supplyAsync(()->{
            try {
                System.out.println("start the acceptEither2");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("end the acceptEither2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  "acceptEither-2";
        }),System.out::println);

    }


    /**
     * {@link CompletableFuture#thenAcceptBoth(CompletionStage, BiConsumer)}
     */
    private static  void thenAcceptBoth(){
        CompletableFuture.supplyAsync(()->{
                    try {
                        System.out.println("start the supplyAsync");
                        TimeUnit.SECONDS.sleep(5);
                        System.out.println("end the supplyAsync");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return  "thenAcceptBoth";
                    // 组合
                }).thenAcceptBoth(CompletableFuture.supplyAsync(()->{
                    try {
                        System.out.println("start the thenAcceptBoth");
                        TimeUnit.SECONDS.sleep(3);
                        System.out.println("end the thenAcceptBoth");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return  100;
                }),(s, i)->System.out.print(s + "----" + i));

    }















}
