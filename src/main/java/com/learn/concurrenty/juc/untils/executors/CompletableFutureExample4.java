package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @ClassName: CompletableFutureExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/15 16:15
 * History:
 * @<version> 1.0
 */
public class CompletableFutureExample4 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        completeExceptionally();
    }


    /**
     * {@link CompletableFuture#completeExceptionally(Throwable)}
     */
    private static  void completeExceptionally() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleepSeconds(5);
            System.out.println("===========================i will be still process...");
            return "HELLo";
        });
        // 因为是异步的，所以在这里进行休眠，让其CompletableFuture 执行一定发生在 下面代码 的前面
        sleepSeconds(1);
        // 立即返回的机制
        future.completeExceptionally(new RuntimeException());
        System.out.println(future.get());
    }


    /**
     * {@link CompletableFuture#join()}
     */
    private static  void testJoin() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleepSeconds(5);
            System.out.println("===========================i will be still process...");
            return "HELLo";
        });
        String result = future.join();
        System.out.println(result);
    }


    /**
     * {@link CompletableFuture#complete(Object)}
     */
    private static  void complete() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleepSeconds(5);
            System.out.println("===========================i will be still process...");
            return "HELLo";
        });
        // 已经转换了 就是true
        boolean world = future.complete("WORLD");
        System.out.println(world);
        System.out.println(future.get());

    }

    private static  void  getNow() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleepSeconds(5);
            return "HELLo";
        });
        String result = future.getNow("WORLD");
        System.out.println(result);
        System.out.println(future.get());

    }

    
    private static  void  sleepSeconds(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
